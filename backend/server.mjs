import http from 'node:http';
import fs from 'node:fs';
import path from 'node:path';
import { fileURLToPath } from 'node:url';
import { spawn, execSync } from 'node:child_process';
import crypto from 'node:crypto';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const PORT = Number(process.env.PORT || 8080);
const HOST = process.env.HOST || '0.0.0.0';
const STORAGE_ROOT = path.resolve(process.env.STORAGE_ROOT || '/mnt/nas/drive');
const FRONTEND_DIR = process.env.FRONTEND_DIR || (
  fs.existsSync(path.join(__dirname, '../frontend'))
    ? path.join(__dirname, '../frontend')
    : (fs.existsSync(path.join(__dirname, 'public'))
        ? path.join(__dirname, 'public')
        : path.join(__dirname, '../public'))
);
const PUBLIC_DIR = FRONTEND_DIR;
const TRASH_DIR = path.join(STORAGE_ROOT, '.trash');
const CHUNKS_DIR = path.join(STORAGE_ROOT, '.chunks');
const THUMBNAILS_DIR = path.join(STORAGE_ROOT, '.thumbnails');
const ADMIN_SECRET_PATH = process.env.ADMIN_SECRET_PATH || (
  fs.existsSync(path.join(__dirname, '.admin_secret'))
    ? path.join(__dirname, '.admin_secret')
    : path.join(__dirname, '../.admin_secret')
);

// Khởi tạo thư mục cần thiết
for (const dir of [STORAGE_ROOT, TRASH_DIR, CHUNKS_DIR, THUMBNAILS_DIR, PUBLIC_DIR]) {
  if (!fs.existsSync(dir)) fs.mkdirSync(dir, { recursive: true });
}

// Khởi tạo hoặc đọc Admin Secret bảo vệ các API quản trị
let ADMIN_SECRET = '';
try {
  if (fs.existsSync(ADMIN_SECRET_PATH)) {
    ADMIN_SECRET = fs.readFileSync(ADMIN_SECRET_PATH, 'utf8').trim();
  }
  if (!ADMIN_SECRET || ADMIN_SECRET.length < 16) {
    ADMIN_SECRET = crypto.randomBytes(32).toString('hex');
    fs.writeFileSync(ADMIN_SECRET_PATH, ADMIN_SECRET, { mode: 0o600, encoding: 'utf8' });
  }
} catch (e) {
  console.error('[NAS Security] Không thể đọc/ghi .admin_secret:', e.message);
  ADMIN_SECRET = crypto.randomBytes(32).toString('hex');
}

// =========================================================================
// USER AUTHENTICATION & SESSION MANAGEMENT
// =========================================================================
const USERS_FILE_PATH = path.join(__dirname, 'users.json');
const SESSIONS_FILE_PATH = path.join(__dirname, 'sessions.json');
const sessions = new Map();
const SESSION_TTL_MS = 30 * 24 * 60 * 60 * 1000; // 30 ngày

function hashPassword(password, salt = crypto.randomBytes(16).toString('hex')) {
  const derived = crypto.scryptSync(password, salt, 64, { N: 16384, r: 8, p: 1 });
  return `scrypt$${salt}$${derived.toString('hex')}`;
}

function verifyPassword(password, storedHash) {
  if (!password || !storedHash) return false;
  try {
    if (storedHash.startsWith('scrypt$')) {
      const parts = storedHash.split('$');
      if (parts.length !== 3) return false;
      const salt = parts[1];
      const hash = parts[2];
      const testHash = crypto.scryptSync(password, salt, 64, { N: 16384, r: 8, p: 1 }).toString('hex');
      return safeCompare(testHash, hash);
    }
  } catch (err) {
    console.error('[Auth] Lỗi xác thực mật khẩu:', err.message);
  }
  return false;
}

function loadUsers() {
  try {
    if (fs.existsSync(USERS_FILE_PATH)) {
      const raw = fs.readFileSync(USERS_FILE_PATH, 'utf8');
      const data = JSON.parse(raw);
      if (Array.isArray(data) && data.length > 0) {
        return data;
      }
    }
  } catch (err) {
    console.error('[Auth] Lỗi đọc users.json:', err.message);
  }

  // Khởi tạo tài khoản mặc định ban đầu nếu chưa có
  const defaultAdmin = {
    id: 'user_admin',
    username: 'admin',
    password_hash: hashPassword('Admin@NAS2026!'),
    full_name: 'Quản trị viên',
    role: 'admin',
    created_at: new Date().toISOString()
  };
  const list = [defaultAdmin];
  try {
    fs.writeFileSync(USERS_FILE_PATH, JSON.stringify(list, null, 2), { mode: 0o600, encoding: 'utf8' });
    console.log('[Auth] Đã khởi tạo tài khoản quản trị mặc định: admin / Admin@NAS2026!');
  } catch (e) {
    console.error('[Auth] Không thể ghi users.json:', e.message);
  }
  return list;
}

function saveUsers(list) {
  try {
    fs.writeFileSync(USERS_FILE_PATH, JSON.stringify(list, null, 2), { mode: 0o600, encoding: 'utf8' });
  } catch (e) {
    console.error('[Auth] Lỗi lưu users.json:', e.message);
  }
}

let usersList = loadUsers();

function loadSessions() {
  try {
    if (fs.existsSync(SESSIONS_FILE_PATH)) {
      const raw = fs.readFileSync(SESSIONS_FILE_PATH, 'utf8');
      const data = JSON.parse(raw);
      const now = Date.now();
      for (const [tok, s] of Object.entries(data)) {
        if (s && s.expiresAt > now) {
          sessions.set(tok, s);
        }
      }
    }
  } catch (_) {}
}

function saveSessions() {
  try {
    const obj = {};
    for (const [tok, s] of sessions.entries()) {
      obj[tok] = s;
    }
    fs.writeFileSync(SESSIONS_FILE_PATH, JSON.stringify(obj, null, 2), { mode: 0o600, encoding: 'utf8' });
  } catch (_) {}
}

loadSessions();

function createSession(user) {
  const token = crypto.randomBytes(32).toString('hex');
  const now = Date.now();
  const sessionData = {
    token,
    user: {
      id: user.id,
      username: user.username,
      full_name: user.full_name || user.username,
      role: user.role || 'user'
    },
    expiresAt: now + SESSION_TTL_MS
  };
  sessions.set(token, sessionData);
  saveSessions();
  return sessionData;
}

function parseCookies(req) {
  const list = {};
  const rc = req.headers.cookie;
  if (!rc) return list;
  for (const cookie of rc.split(';')) {
    const parts = cookie.split('=');
    if (parts.length >= 2) {
      list[parts[0].trim()] = decodeURIComponent(parts.slice(1).join('=').trim());
    }
  }
  return list;
}

function getAuthUser(req, url) {
  // 1. Kiểm tra Admin Key (CLI hoặc Bot Telegram)
  const providedKey = req.headers['x-admin-key'] || (url ? url.searchParams.get('admin_key') : '');
  if (providedKey && safeCompare(providedKey, ADMIN_SECRET)) {
    return { id: 'admin_key', username: 'admin', role: 'admin', full_name: 'Admin Key' };
  }

  // 2. Lấy token từ Authorization: Bearer, Cookie hoặc Query ?token=
  let token = '';
  const authHeader = req.headers.authorization;
  if (authHeader && authHeader.toLowerCase().startsWith('bearer ')) {
    token = authHeader.slice(7).trim();
  }
  if (!token) {
    const cookies = parseCookies(req);
    token = cookies.nas_session;
  }
  if (!token && url) {
    token = url.searchParams.get('token') || '';
  }

  if (!token) return null;

  const session = sessions.get(token);
  if (!session) return null;

  const now = Date.now();
  if (now > session.expiresAt) {
    sessions.delete(token);
    saveSessions();
    return null;
  }

  // Gia hạn session nếu đã dùng quá nửa thời hạn
  if (session.expiresAt - now < SESSION_TTL_MS / 2) {
    session.expiresAt = now + SESSION_TTL_MS;
    saveSessions();
  }

  return session.user;
}

// Rate Limiting bảo vệ Endpoint Login chống brute-force
const failedLoginMap = new Map();
function checkLoginAttempt(ip) {
  const now = Date.now();
  const rec = failedLoginMap.get(ip);
  if (!rec) return { allowed: true };
  if (rec.lockUntil && now < rec.lockUntil) {
    const remainingSec = Math.ceil((rec.lockUntil - now) / 1000);
    return {
      allowed: false,
      error: `Tài khoản tạm thời bị khóa do nhập sai nhiều lần. Vui lòng thử lại sau ${remainingSec} giây.`
    };
  }
  if (rec.lockUntil && now >= rec.lockUntil) {
    failedLoginMap.delete(ip);
    return { allowed: true };
  }
  return { allowed: true };
}

function recordLoginFailed(ip) {
  const now = Date.now();
  let rec = failedLoginMap.get(ip);
  if (!rec) {
    rec = { count: 1, lastAttempt: now };
    failedLoginMap.set(ip, rec);
  } else {
    rec.count++;
    rec.lastAttempt = now;
  }
  if (rec.count >= 5) {
    rec.lockUntil = now + 60 * 1000; // Khóa 60s sau 5 lần sai
  }
}

function recordLoginSuccess(ip) {
  failedLoginMap.delete(ip);
}

// =========================================================================
// THƯ MỤC BÍ MẬT KHÓA ẨN (SECURE HIDDEN VAULT)
// =========================================================================
const VAULT_DIR = path.join(STORAGE_ROOT, '.vault');
const VAULT_STORAGE_DIR = path.join(VAULT_DIR, 'storage'); // Nơi lưu trữ tệp & thư mục khóa ẩn
const VAULT_ITEMS_DIR = path.join(VAULT_DIR, 'items');     // Tương thích ngược tệp mã hóa cũ
const VAULT_CONFIG_PATH = path.join(__dirname, 'vault-config.json');

for (const dir of [VAULT_DIR, VAULT_STORAGE_DIR, VAULT_ITEMS_DIR]) {
  if (!fs.existsSync(dir)) fs.mkdirSync(dir, { recursive: true });
}

// Giải quyết đường dẫn tuyệt đối an toàn bên trong Thư mục khóa ẩn (VAULT_STORAGE_DIR)
function safeVaultResolve(relPath = '', options = {}) {
  const { mustExist = false } = options;
  if (typeof relPath !== 'string') return null;
  if (relPath.includes('\0')) return null;

  let norm = path.normalize(relPath).replace(/\\/g, '/');
  norm = norm.replace(/^(\.\.[\/\\])+/, '').replace(/^[\/\\]+/, '');

  const parts = norm.split('/').filter(Boolean);
  if (parts.includes('..')) return null;

  const abs = path.resolve(VAULT_STORAGE_DIR, norm);
  if (abs !== VAULT_STORAGE_DIR && !abs.startsWith(VAULT_STORAGE_DIR + path.sep)) {
    return null;
  }

  if (fs.existsSync(abs)) {
    try {
      const real = fs.realpathSync(abs);
      if (real !== VAULT_STORAGE_DIR && !real.startsWith(VAULT_STORAGE_DIR + path.sep)) {
        return null;
      }
      return real;
    } catch (_) {
      return null;
    }
  } else if (mustExist) {
    return null;
  }
  return abs;
}

// Map lưu trữ Vault session đã mở khóa trong RAM: userId -> { key: Buffer, expiresAt: number }
const vaultSessions = new Map();
const VAULT_TTL_MS = 15 * 60 * 1000; // 15 phút không hoạt động tự động khóa

function getVaultConfig() {
  try {
    if (fs.existsSync(VAULT_CONFIG_PATH)) {
      return JSON.parse(fs.readFileSync(VAULT_CONFIG_PATH, 'utf8'));
    }
  } catch (_) {}
  return null;
}

function saveVaultConfig(cfg) {
  fs.writeFileSync(VAULT_CONFIG_PATH, JSON.stringify(cfg, null, 2), { mode: 0o600, encoding: 'utf8' });
}

function deriveVaultKey(password, salt) {
  return crypto.scryptSync(password, salt, 32, { N: 16384, r: 8, p: 1 });
}

function getActiveVaultKey(userId) {
  if (!userId) return null;
  const sess = vaultSessions.get(userId);
  if (!sess) return null;
  const now = Date.now();
  if (now > sess.expiresAt) {
    vaultSessions.delete(userId);
    return null;
  }
  sess.expiresAt = now + VAULT_TTL_MS;
  return sess.key;
}

// Cấu trúc đóng gói file mã hóa chuẩn AES-256-GCM:
// [4B Magic: 'NASV'][1B Version: 1][12B IV][16B AuthTag][4B MetaLen BE][MetaEncrypted][PayloadEncrypted]
function encryptVaultFile(buffer, meta, key) {
  const iv = crypto.randomBytes(12);
  const cipher = crypto.createCipheriv('aes-256-gcm', key, iv);
  const encPayload = Buffer.concat([cipher.update(buffer), cipher.final()]);
  const authTag = cipher.getAuthTag();

  const metaIv = crypto.randomBytes(12);
  const metaCipher = crypto.createCipheriv('aes-256-gcm', key, metaIv);
  const metaBuf = Buffer.from(JSON.stringify(meta), 'utf8');
  const encMeta = Buffer.concat([metaCipher.update(metaBuf), metaCipher.final()]);
  const metaTag = metaCipher.getAuthTag();

  const metaBlock = Buffer.concat([metaIv, metaTag, encMeta]);
  const metaLenBuf = Buffer.alloc(4);
  metaLenBuf.writeUInt32BE(metaBlock.length, 0);

  const magic = Buffer.from('NASV', 'ascii');
  const version = Buffer.from([1]);

  return Buffer.concat([magic, version, iv, authTag, metaLenBuf, metaBlock, encPayload]);
}

function decryptVaultFile(fileBuffer, key) {
  if (fileBuffer.length < 4 + 1 + 12 + 16 + 4 + 12 + 16) {
    throw new Error('Dữ liệu két sắt không hợp lệ hoặc bị hỏng');
  }

  const magic = fileBuffer.subarray(0, 4).toString('ascii');
  if (magic !== 'NASV') throw new Error('Định dạng tệp két sắt không hợp lệ');

  const iv = fileBuffer.subarray(5, 17);
  const authTag = fileBuffer.subarray(17, 33);
  const metaLen = fileBuffer.readUInt32BE(33);

  const metaBlock = fileBuffer.subarray(37, 37 + metaLen);
  const encPayload = fileBuffer.subarray(37 + metaLen);

  // Giải mã metadata
  const metaIv = metaBlock.subarray(0, 12);
  const metaTag = metaBlock.subarray(12, 28);
  const encMeta = metaBlock.subarray(28);

  const metaDecipher = crypto.createDecipheriv('aes-256-gcm', key, metaIv);
  metaDecipher.setAuthTag(metaTag);
  const metaJson = Buffer.concat([metaDecipher.update(encMeta), metaDecipher.final()]).toString('utf8');
  const meta = JSON.parse(metaJson);

  // Giải mã payload
  const decipher = crypto.createDecipheriv('aes-256-gcm', key, iv);
  decipher.setAuthTag(authTag);
  const payload = Buffer.concat([decipher.update(encPayload), decipher.final()]);

  return { meta, payload };
}

// Bộ nhớ đệm metadata của Vault trong RAM để siêu tốc độ và không bao giờ nghẽn I/O
const vaultMetadataCache = new Map(); // fpath -> { mtimeMs, meta }

function readVaultMetadataFromFile(filePath, key) {
  try {
    const stat = fs.statSync(filePath);
    const cached = vaultMetadataCache.get(filePath);
    if (cached && cached.mtimeMs === stat.mtimeMs && cached.meta) {
      return cached.meta;
    }

    if (stat.size < 37) return null;

    const fd = fs.openSync(filePath, 'r');
    try {
      const header = Buffer.allocUnsafe(37);
      const bytesRead = fs.readSync(fd, header, 0, 37, 0);
      if (bytesRead < 37) return null;

      const magic = header.subarray(0, 4).toString('ascii');
      if (magic !== 'NASV') return null;

      const metaLen = header.readUInt32BE(33);
      if (metaLen <= 28 || metaLen > 64 * 1024) return null;

      const metaBlock = Buffer.allocUnsafe(metaLen);
      const metaBytesRead = fs.readSync(fd, metaBlock, 0, metaLen, 37);
      if (metaBytesRead < metaLen) return null;

      const metaIv = metaBlock.subarray(0, 12);
      const metaTag = metaBlock.subarray(12, 28);
      const encMeta = metaBlock.subarray(28);

      const metaDecipher = crypto.createDecipheriv('aes-256-gcm', key, metaIv);
      metaDecipher.setAuthTag(metaTag);
      const metaJson = Buffer.concat([metaDecipher.update(encMeta), metaDecipher.final()]).toString('utf8');
      const meta = JSON.parse(metaJson);

      vaultMetadataCache.set(filePath, { mtimeMs: stat.mtimeMs, meta });
      return meta;
    } finally {
      fs.closeSync(fd);
    }
  } catch (_) {
    return null;
  }
}

function readVaultMetadata(fileBuffer, key) {
  if (fileBuffer.length < 37) return null;
  try {
    const magic = fileBuffer.subarray(0, 4).toString('ascii');
    if (magic !== 'NASV') return null;
    const metaLen = fileBuffer.readUInt32BE(33);
    const metaBlock = fileBuffer.subarray(37, 37 + metaLen);

    const metaIv = metaBlock.subarray(0, 12);
    const metaTag = metaBlock.subarray(12, 28);
    const encMeta = metaBlock.subarray(28);

    const metaDecipher = crypto.createDecipheriv('aes-256-gcm', key, metaIv);
    metaDecipher.setAuthTag(metaTag);
    const metaJson = Buffer.concat([metaDecipher.update(encMeta), metaDecipher.final()]).toString('utf8');
    return JSON.parse(metaJson);
  } catch (_) {
    return null;
  }
}

// Stream Encryption: Mã hóa file dung lượng bất kỳ (kể cả tệp >2GB, 10GB, 50GB) với bộ nhớ RAM < 1MB
function encryptVaultFileStream(sourcePath, destPath, meta, key) {
  return new Promise((resolve, reject) => {
    let destFd = null;
    try {
      destFd = fs.openSync(destPath, 'w');
      const iv = crypto.randomBytes(12);
      const cipher = crypto.createCipheriv('aes-256-gcm', key, iv);

      const metaIv = crypto.randomBytes(12);
      const metaCipher = crypto.createCipheriv('aes-256-gcm', key, metaIv);
      const metaBuf = Buffer.from(JSON.stringify(meta), 'utf8');
      const encMeta = Buffer.concat([metaCipher.update(metaBuf), metaCipher.final()]);
      const metaTag = metaCipher.getAuthTag();
      const metaBlock = Buffer.concat([metaIv, metaTag, encMeta]);

      const metaLenBuf = Buffer.alloc(4);
      metaLenBuf.writeUInt32BE(metaBlock.length, 0);

      const magic = Buffer.from('NASV', 'ascii');
      const version = Buffer.from([1]);
      const authTagPlaceholder = Buffer.alloc(16, 0);

      const header = Buffer.concat([magic, version, iv, authTagPlaceholder, metaLenBuf, metaBlock]);
      fs.writeSync(destFd, header, 0, header.length, 0);

      const readStream = fs.createReadStream(sourcePath, { highWaterMark: 1024 * 1024 });
      const destWriteStream = fs.createWriteStream(null, { fd: destFd, start: header.length, autoClose: false });

      readStream.pipe(cipher).pipe(destWriteStream);

      destWriteStream.on('finish', () => {
        try {
          const authTag = cipher.getAuthTag();
          fs.writeSync(destFd, authTag, 0, 16, 17);
          fs.closeSync(destFd);
          destFd = null;
          resolve(true);
        } catch (e) {
          if (destFd !== null) { try { fs.closeSync(destFd); } catch (_) {} }
          reject(e);
        }
      });

      readStream.on('error', err => {
        if (destFd !== null) { try { fs.closeSync(destFd); } catch (_) {} }
        reject(err);
      });
      cipher.on('error', err => {
        if (destFd !== null) { try { fs.closeSync(destFd); } catch (_) {} }
        reject(err);
      });
      destWriteStream.on('error', err => {
        if (destFd !== null) { try { fs.closeSync(destFd); } catch (_) {} }
        reject(err);
      });
    } catch (err) {
      if (destFd !== null) { try { fs.closeSync(destFd); } catch (_) {} }
      reject(err);
    }
  });
}

// Stream Decryption: Giải mã file dung lượng bất kỳ ra file đích với bộ nhớ RAM < 1MB
function decryptVaultFileStream(sourceEncPath, destPlainPath, key) {
  return new Promise((resolve, reject) => {
    try {
      const fd = fs.openSync(sourceEncPath, 'r');
      const header = Buffer.alloc(37);
      fs.readSync(fd, header, 0, 37, 0);
      const magic = header.subarray(0, 4).toString('ascii');
      if (magic !== 'NASV') {
        fs.closeSync(fd);
        return reject(new Error('Định dạng tệp két sắt không hợp lệ'));
      }
      const iv = header.subarray(5, 17);
      const authTag = header.subarray(17, 33);
      const metaLen = header.readUInt32BE(33);
      const payloadOffset = 37 + metaLen;
      fs.closeSync(fd);

      const decipher = crypto.createDecipheriv('aes-256-gcm', key, iv);
      decipher.setAuthTag(authTag);

      const readStream = fs.createReadStream(sourceEncPath, { start: payloadOffset, highWaterMark: 1024 * 1024 });
      const writeStream = fs.createWriteStream(destPlainPath);

      readStream.pipe(decipher).pipe(writeStream);
      writeStream.on('finish', () => resolve(true));
      readStream.on('error', reject);
      decipher.on('error', reject);
      writeStream.on('error', reject);
    } catch (e) {
      reject(e);
    }
  });
}

const mimeTypes = {
  '.html': 'text/html; charset=utf-8',
  '.css': 'text/css; charset=utf-8',
  '.js': 'text/javascript; charset=utf-8',
  '.mjs': 'text/javascript; charset=utf-8',
  '.json': 'application/json; charset=utf-8',
  '.svg': 'image/svg+xml',
  '.png': 'image/png',
  '.jpg': 'image/jpeg',
  '.jpeg': 'image/jpeg',
  '.webp': 'image/webp',
  '.gif': 'image/gif',
  '.mp4': 'video/mp4',
  '.webm': 'video/webm',
  '.mov': 'video/quicktime',
  '.mkv': 'video/x-matroska',
  '.avi': 'video/x-msvideo',
  '.mp3': 'audio/mpeg',
  '.wav': 'audio/wav',
  '.flac': 'audio/flac',
  '.m4a': 'audio/mp4',
  '.ogg': 'audio/ogg',
  '.pdf': 'application/pdf',
  '.zip': 'application/zip',
  '.txt': 'text/plain; charset=utf-8',
  '.md': 'text/markdown; charset=utf-8',
  '.csv': 'text/csv; charset=utf-8',
  '.log': 'text/plain; charset=utf-8',
  '.xml': 'application/xml; charset=utf-8',
  '.sh': 'text/x-sh; charset=utf-8',
  '.py': 'text/x-python; charset=utf-8',
  '.yaml': 'text/yaml; charset=utf-8',
  '.yml': 'text/yaml; charset=utf-8'
};

// =========================================================================
// SECURITY & DEFENSE ENGINE
// =========================================================================

// Danh sách Origin tin cậy (CORS whitelist)
const ALLOWED_ORIGIN_PATTERNS = [
  'https://luutru.npccc.io.vn',
  /^http:\/\/192\.168\.\d+\.\d+(:\d+)?$/,
  /^http:\/\/10\.\d+\.\d+\.\d+(:\d+)?$/,
  /^http:\/\/172\.(1[6-9]|2[0-9]|3[0-1])\.\d+\.\d+(:\d+)?$/,
  /^http:\/\/localhost(:\d+)?$/,
  /^http:\/\/127\.0\.0\.1(:\d+)?$/
];

function isOriginAllowed(origin) {
  if (!origin) return true; // Cho phép Mobile Native App, cURL, LAN direct
  return ALLOWED_ORIGIN_PATTERNS.some(pat => {
    if (typeof pat === 'string') return pat === origin;
    if (pat instanceof RegExp) return pat.test(origin);
    return false;
  });
}

function getClientIp(req) {
  const cfIp = req.headers['cf-connecting-ip'];
  if (cfIp) return cfIp.trim();
  const xff = req.headers['x-forwarded-for'];
  if (xff) return xff.split(',')[0].trim();
  return (req.socket?.remoteAddress || '').replace(/^::ffff:/, '');
}

function isLanIp(ip) {
  if (!ip) return false;
  if (ip === '127.0.0.1' || ip === '::1' || ip === 'localhost') return true;
  if (ip.startsWith('192.168.')) return true;
  if (ip.startsWith('10.')) return true;
  if (/^172\.(1[6-9]|2[0-9]|3[0-1])\./.test(ip)) return true;
  return false;
}

function safeCompare(a, b) {
  if (typeof a !== 'string' || typeof b !== 'string') return false;
  const bufA = Buffer.from(a);
  const bufB = Buffer.from(b);
  if (bufA.length !== bufB.length) return false;
  return crypto.timingSafeEqual(bufA, bufB);
}

// Kiểm tra quyền hạn quản trị viên (Admin)
function checkAdminAuth(req, url) {
  const ip = getClientIp(req);
  const isDirectLan = isLanIp(ip) && !req.headers['cf-connecting-ip'];
  const providedKey = req.headers['x-admin-key'] || url.searchParams.get('admin_key') || '';

  if (safeCompare(providedKey, ADMIN_SECRET)) {
    return { ok: true, isLan: isDirectLan };
  }
  // Nếu truy cập từ mạng LAN nội bộ
  if (isDirectLan) {
    return { ok: true, isLan: true };
  }
  return { ok: false, isLan: false };
}

// Rate Limiting theo Sliding Window
const rateLimitMap = new Map();
function checkRateLimit(req, isSensitive = false) {
  const ip = getClientIp(req);
  const now = Date.now();
  const windowMs = 10000; // 10 giây
  const maxRequests = isSensitive ? 30 : 250;

  let record = rateLimitMap.get(ip);
  if (!record || now - record.startTime > windowMs) {
    rateLimitMap.set(ip, { count: 1, startTime: now });
    return true;
  }

  record.count++;
  return record.count <= maxRequests;
}

// Dọn dẹp RAM rate limit mỗi 60 giây
setInterval(() => {
  const now = Date.now();
  for (const [ip, rec] of rateLimitMap.entries()) {
    if (now - rec.startTime > 30000) rateLimitMap.delete(ip);
  }
}, 60000);

// Thêm các Header bảo mật chuẩn OWASP
function applySecurityHeaders(res, origin = '') {
  if (res.headersSent) return;
  res.setHeader('X-Content-Type-Options', 'nosniff');
  res.setHeader('X-Frame-Options', 'SAMEORIGIN');
  res.setHeader('X-XSS-Protection', '1; mode=block');
  res.setHeader('Referrer-Policy', 'strict-origin-when-cross-origin');
  res.setHeader('Permissions-Policy', 'geolocation=(), camera=(), microphone=()');

  if (origin && isOriginAllowed(origin)) {
    res.setHeader('Access-Control-Allow-Origin', origin);
    res.setHeader('Access-Control-Allow-Credentials', 'true');
    res.setHeader('Vary', 'Origin');
  }
}

// Đọc JSON an toàn với giới hạn dung lượng nghiêm ngặt (Chống DoS Heap OOM)
function readJsonBody(req, res, maxBytes = 1024 * 1024) {
  return new Promise((resolve, reject) => {
    let body = '';
    let bytes = 0;
    req.on('data', chunk => {
      bytes += chunk.length;
      if (bytes > maxBytes) {
        req.destroy();
        reject(new Error('PAYLOAD_TOO_LARGE'));
      } else {
        body += chunk;
      }
    });
    req.on('end', () => {
      try {
        const data = JSON.parse(body || '{}');
        resolve(data);
      } catch (_) {
        reject(new Error('INVALID_JSON'));
      }
    });
    req.on('error', reject);
  });
}

// Giải quyết đường dẫn tuyệt đối an toàn - Chống triệt để Path Traversal & Symlink Escape
function safeResolve(relPath = '', options = {}) {
  const { allowTrash = false, allowSystem = false, mustExist = false } = options;
  if (typeof relPath !== 'string') return null;
  if (relPath.includes('\0')) return null;

  let norm = path.normalize(relPath).replace(/\\/g, '/');
  norm = norm.replace(/^(\.\.[\/\\])+/, '').replace(/^[\/\\]+/, '');

  const parts = norm.split('/').filter(Boolean);
  if (parts.includes('..')) return null;

  // Bảo vệ thư mục hệ thống bảo lưu
  if (!allowSystem && parts.length > 0) {
    const first = parts[0].toLowerCase();
    if (first === '.trash' && !allowTrash) return null;
    if (first === '.chunks' || first === '.thumbnails' || first === '.vault') return null;
  }

  const abs = path.resolve(STORAGE_ROOT, norm);
  if (abs !== STORAGE_ROOT && !abs.startsWith(STORAGE_ROOT + path.sep)) {
    return null;
  }

  // Chống Symlink Traversal Escape nếu file/thư mục đã tồn tại
  if (fs.existsSync(abs)) {
    try {
      const real = fs.realpathSync(abs);
      if (real !== STORAGE_ROOT && !real.startsWith(STORAGE_ROOT + path.sep)) {
        return null;
      }
      return real;
    } catch (_) {
      return null;
    }
  } else if (mustExist) {
    return null;
  }

  return abs;
}

function getFileType(filename, isFolder) {
  if (isFolder) return 'folder';
  const ext = path.extname(filename).toLowerCase();
  if (['.mp4', '.m4v', '.webm', '.mov', '.mkv', '.avi', '.ts', '.flv', '.wmv'].includes(ext)) return 'video';
  if (['.jpg', '.jpeg', '.png', '.webp', '.gif', '.svg', '.bmp', '.heic', '.raw'].includes(ext)) return 'image';
  if (['.mp3', '.wav', '.flac', '.m4a', '.aac', '.ogg', '.wma'].includes(ext)) return 'audio';
  if (['.pdf', '.doc', '.docx', '.xls', '.xlsx', '.ppt', '.pptx', '.txt', '.md'].includes(ext)) return 'document';
  if (['.zip', '.rar', '.7z', '.tar', '.gz'].includes(ext)) return 'archive';
  return 'other';
}

// Bảng theo dõi các tệp đang upload dở dang để tránh xung đột tên khi upload đồng thời
const activeReservedPaths = new Set();

// Chống vỡ tiếng Việt, tự động đổi tên khi trùng & giới hạn byte an toàn cho Linux ext4 (NAME_MAX = 255 bytes)
function getSafeUniqueFilePath(targetDir, rawFilename) {
  let normalized = (rawFilename || 'file').normalize('NFC').trim();
  // Loại bỏ các ký tự điều khiển nguy hiểm và dấu phân cách thư mục
  normalized = normalized.replace(/[\x00-\x1F\x7F]/g, '').replace(/[\/\\]/g, '_');
  const ext = path.extname(normalized);
  let base = path.basename(normalized, ext);

  // Nhận diện nếu tên file gốc đã có hậu tố dạng "(số)" ví dụ: "tài liệu (1)"
  const match = base.match(/^(.*?)\s*\((\d+)\)$/);
  let stem = base;
  let counter = 1;
  if (match) {
    stem = match[1].trim();
    counter = parseInt(match[2], 10) + 1;
  }

  const maxBaseBytes = Math.max(10, 200 - Buffer.byteLength(ext, 'utf8'));
  while (Buffer.byteLength(stem, 'utf8') > maxBaseBytes && stem.length > 0) {
    stem = stem.slice(0, -1);
  }
  let safeStem = stem || 'file';

  // Thử tên ban đầu
  let finalName = `${base}${ext}`;
  let finalPath = path.join(targetDir, finalName);

  // Nếu file đã tồn tại trên đĩa HOẶC đang được một luồng upload khác giữ chỗ tên này
  while (fs.existsSync(finalPath) || activeReservedPaths.has(finalPath)) {
    const suffix = ` (${counter})`;
    const subMax = maxBaseBytes - Buffer.byteLength(suffix, 'utf8');
    let subBase = safeStem;
    while (Buffer.byteLength(subBase, 'utf8') > subMax && subBase.length > 0) {
      subBase = subBase.slice(0, -1);
    }
    finalName = `${subBase}${suffix}${ext}`;
    finalPath = path.join(targetDir, finalName);
    counter++;
  }

  // Khóa giữ chỗ tên tệp này trong RAM
  activeReservedPaths.add(finalPath);

  const tempName = `.tmp_${Date.now()}_${crypto.randomBytes(4).toString('hex')}`;
  const tempPath = path.join(targetDir, tempName);

  const releaseReservation = () => {
    activeReservedPaths.delete(finalPath);
  };

  return { finalName, finalPath, tempPath, releaseReservation };
}

// Chuyển đổi nền các tệp mã hóa cũ sang Thư mục khóa ẩn (VAULT_STORAGE_DIR)
let isMigrationRunning = false;
async function startBackgroundMigration(key) {
  if (isMigrationRunning) return;
  isMigrationRunning = true;
  try {
    if (!fs.existsSync(VAULT_ITEMS_DIR)) return;
    const files = fs.readdirSync(VAULT_ITEMS_DIR).filter(f => f.endsWith('.enc'));
    if (files.length === 0) return;

    console.log(`[Vault Migration] Bắt đầu chuyển ${files.length} tệp mã hóa cũ sang Thư mục khóa ẩn...`);
    for (const f of files) {
      const fpath = path.join(VAULT_ITEMS_DIR, f);
      if (!fs.existsSync(fpath)) continue;
      try {
        const meta = readVaultMetadataFromFile(fpath, key);
        if (meta && meta.name) {
          const destRel = meta.name;
          const destPath = path.resolve(VAULT_STORAGE_DIR, destRel);
          if (destPath.startsWith(VAULT_STORAGE_DIR)) {
            fs.mkdirSync(path.dirname(destPath), { recursive: true });
            const { finalPath, releaseReservation } = getSafeUniqueFilePath(path.dirname(destPath), path.basename(destPath));
            const st = fs.statSync(fpath);
            if (st.size > 30 * 1024 * 1024) {
              await decryptVaultFileStream(fpath, finalPath, key);
            } else {
              const raw = fs.readFileSync(fpath);
              const decrypted = decryptVaultFile(raw, key);
              fs.writeFileSync(finalPath, decrypted.payload);
            }
            releaseReservation();
            fs.unlinkSync(fpath);
            vaultMetadataCache.delete(fpath);
          }
        }
      } catch (err) {
        console.error(`[Vault Migration] Lỗi tệp ${f}:`, err.message);
      }
      await new Promise(r => setTimeout(r, 10));
    }
    console.log('[Vault Migration] Hoàn tất chuyển đổi tất cả tệp cũ sang Thư mục khóa ẩn.');
    broadcastEvent('file_changed', {});
  } catch (e) {
    console.error('[Vault Migration Fatal]:', e);
  } finally {
    isMigrationRunning = false;
  }
}

// Bắt lỗi toàn cục để server không bao giờ bị crash
process.on('uncaughtException', err => {
  console.error('[NAS UncaughtException]:', err.message);
});
process.on('unhandledRejection', reason => {
  console.error('[NAS UnhandledRejection]:', reason);
});

function sendJson(res, statusCode, data, origin = '') {
  if (res.headersSent || res.writableEnded) return;
  const payload = JSON.stringify(data);
  const headers = {
    'Content-Type': 'application/json; charset=utf-8',
    'Content-Length': Buffer.byteLength(payload),
    'X-Content-Type-Options': 'nosniff',
    'X-Frame-Options': 'SAMEORIGIN',
    'X-XSS-Protection': '1; mode=block',
    'Referrer-Policy': 'strict-origin-when-cross-origin'
  };

  if (origin && isOriginAllowed(origin)) {
    headers['Access-Control-Allow-Origin'] = origin;
    headers['Access-Control-Allow-Credentials'] = 'true';
    headers['Vary'] = 'Origin';
  }

  // Bảo toàn các Header nhạy cảm đã gắn sẵn trên res (như Set-Cookie)
  const existingSetCookie = res.getHeader('Set-Cookie');
  if (existingSetCookie) {
    headers['Set-Cookie'] = existingSetCookie;
  }

  res.writeHead(statusCode, headers);
  res.end(payload);
}

function formatBytes(bytes) {
  if (!bytes || bytes <= 0) return '0 B';
  const k = 1024;
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return parseFloat((bytes / Math.pow(k, i)).toFixed(1)) + ' ' + sizes[i];
}

const dateFormatter = new Intl.DateTimeFormat('vi-VN', {
  year: 'numeric', month: '2-digit', day: '2-digit',
  hour: '2-digit', minute: '2-digit', second: '2-digit',
  hour12: false
});

const nameCollator = new Intl.Collator('vi', { numeric: true, sensitivity: 'base' });

// =========================================================================
// IN-MEMORY FOLDER LISTING CACHE (Phản hồi tức thì 0ms khi duyệt thư mục)
// =========================================================================
const folderListCache = new Map();
const FOLDER_CACHE_TTL_MS = 30000; // Cache 30 giây

function invalidateFolderCache() {
  folderListCache.clear();
}

async function scanDirRecursive(dirPath, maxDepth = 3, currentDepth = 0) {
  if (currentDepth > maxDepth || !fs.existsSync(dirPath)) return [];
  try {
    const entries = await fs.promises.readdir(dirPath, { withFileTypes: true });
    const valid = entries.filter(ent => !ent.name.startsWith('.'));

    const items = await Promise.all(valid.map(async ent => {
      const fullPath = path.join(dirPath, ent.name);
      const isFolder = ent.isDirectory();
      const relPath = path.relative(STORAGE_ROOT, fullPath).replace(/\\/g, '/');
      let size = 0;
      let mtime = 0;
      try {
        const st = await fs.promises.stat(fullPath);
        size = st.size;
        mtime = st.mtimeMs;
      } catch (_) {}

      const item = {
        name: ent.name,
        path: relPath,
        isFolder,
        size,
        formattedSize: isFolder ? '--' : formatBytes(size),
        mtime,
        formattedDate: dateFormatter.format(mtime),
        type: getFileType(ent.name, isFolder),
        ext: path.extname(ent.name).toLowerCase()
      };

      if (isFolder && currentDepth < maxDepth) {
        const subItems = await scanDirRecursive(fullPath, maxDepth, currentDepth + 1);
        return [item, ...subItems];
      }
      return [item];
    }));

    return items.flat();
  } catch (_) {
    return [];
  }
}

function handleStream(req, res, targetPath, filename) {
  if (res.headersSent || res.writableEnded) return;
  const stat = fs.statSync(targetPath);
  const fileSize = stat.size;
  const range = req.headers.range;
  const ext = path.extname(filename).toLowerCase();
  const contentType = mimeTypes[ext] || 'application/octet-stream';
  const origin = req.headers.origin;

  const baseHeaders = {
    'Content-Type': contentType,
    'Accept-Ranges': 'bytes',
    'Cache-Control': 'public, max-age=3600',
    'X-Content-Type-Options': 'nosniff',
    'X-Frame-Options': 'SAMEORIGIN'
  };

  if (origin && isOriginAllowed(origin)) {
    baseHeaders['Access-Control-Allow-Origin'] = origin;
    baseHeaders['Access-Control-Allow-Credentials'] = 'true';
    baseHeaders['Vary'] = 'Origin';
  }

  if (range) {
    const parts = range.replace(/bytes=/, '').split('-');
    const start = parseInt(parts[0], 10);
    const end = parts[1] ? parseInt(parts[1], 10) : fileSize - 1;

    if (isNaN(start) || isNaN(end) || start >= fileSize || end >= fileSize || start > end) {
      res.writeHead(416, { 'Content-Range': `bytes */${fileSize}` });
      return res.end();
    }

    const chunksize = (end - start) + 1;
    res.writeHead(206, {
      ...baseHeaders,
      'Content-Range': `bytes ${start}-${end}/${fileSize}`,
      'Content-Length': chunksize
    });
    fs.createReadStream(targetPath, { start, end, highWaterMark: 1024 * 1024 }).pipe(res);
  } else {
    res.writeHead(200, {
      ...baseHeaders,
      'Content-Length': fileSize
    });
    fs.createReadStream(targetPath, { highWaterMark: 1024 * 1024 }).pipe(res);
  }
}

// Dọn dẹp chunk & file tạm cũ
function cleanStaleFiles() {
  try {
    const now = Date.now();
    if (fs.existsSync(CHUNKS_DIR)) {
      const sessions = fs.readdirSync(CHUNKS_DIR);
      for (const s of sessions) {
        const full = path.join(CHUNKS_DIR, s);
        try {
          const st = fs.statSync(full);
          if (now - st.mtimeMs > 6 * 3600 * 1000) {
            fs.rmSync(full, { recursive: true, force: true });
          }
        } catch (_) {}
      }
    }
  } catch (_) {}
}
setInterval(cleanStaleFiles, 3600 * 1000);
cleanStaleFiles();

// Hàng đợi tạo ảnh thu nhỏ (Video Thumbnail Queue) tối đa 3 tiến trình đồng thời
const thumbnailQueue = [];
let activeThumbJobs = 0;
const MAX_CONCURRENT_THUMBS = 3;
const pendingThumbPromises = new Map();

function generateThumbnail(targetPath, thumbFile) {
  if (pendingThumbPromises.has(thumbFile)) {
    return pendingThumbPromises.get(thumbFile);
  }
  const promise = new Promise((resolve, reject) => {
    thumbnailQueue.push({ targetPath, thumbFile, resolve, reject });
    processThumbnailQueue();
  }).finally(() => {
    pendingThumbPromises.delete(thumbFile);
  });
  pendingThumbPromises.set(thumbFile, promise);
  return promise;
}

function processThumbnailQueue() {
  if (activeThumbJobs >= MAX_CONCURRENT_THUMBS || thumbnailQueue.length === 0) return;
  const { targetPath, thumbFile, resolve, reject } = thumbnailQueue.shift();
  activeThumbJobs++;

  const runFfmpeg = (args) => new Promise((res, rej) => {
    const child = spawn('ffmpeg', args, { stdio: 'ignore' });
    const timer = setTimeout(() => {
      child.kill('SIGKILL');
      rej(new Error('ffmpeg timeout'));
    }, 15000);
    child.on('close', code => {
      clearTimeout(timer);
      if (code === 0 && fs.existsSync(thumbFile) && fs.statSync(thumbFile).size > 0) {
        res(true);
      } else {
        rej(new Error('ffmpeg exited with code ' + code));
      }
    });
    child.on('error', err => {
      clearTimeout(timer);
      rej(err);
    });
  });

  const primaryArgs = ['-ss', '00:00:01', '-i', targetPath, '-frames:v', '1', '-update', '1', '-vf', 'scale=360:-2', '-q:v', '3', thumbFile, '-y'];
  const fallbackArgs = ['-i', targetPath, '-frames:v', '1', '-update', '1', '-vf', 'scale=360:-2', '-q:v', '3', thumbFile, '-y'];

  runFfmpeg(primaryArgs)
    .catch(() => runFfmpeg(fallbackArgs))
    .then(() => resolve(thumbFile))
    .catch(err => {
      try { if (fs.existsSync(thumbFile)) fs.unlinkSync(thumbFile); } catch (_) {}
      reject(err);
    })
    .finally(() => {
      activeThumbJobs--;
      processThumbnailQueue();
    });
}

// =========================================================================
// REALTIME SERVER-SENT EVENTS (SSE) HUB
// =========================================================================
const sseClients = new Set();

function broadcastEvent(eventType, data = {}) {
  if (eventType === 'file_changed') {
    invalidateFolderCache();
  }
  const payload = `event: ${eventType}\ndata: ${JSON.stringify(data)}\n\n`;
  for (const client of sseClients) {
    try {
      client.write(payload);
    } catch (_) {
      sseClients.delete(client);
    }
  }
}

// =========================================================================
// HTTP SERVER ROUTER
// =========================================================================
const server = http.createServer(async (req, res) => {
  const origin = req.headers.origin;

  // 1. Kiểm tra CORS cho Preflight OPTIONS
  if (req.method === 'OPTIONS') {
    if (origin && !isOriginAllowed(origin)) {
      res.writeHead(403, { 'Content-Type': 'text/plain; charset=utf-8' });
      return res.end('CORS Origin Forbidden');
    }
    const headers = {
      'Access-Control-Allow-Methods': 'GET, POST, PUT, DELETE, OPTIONS',
      'Access-Control-Allow-Headers': 'Content-Type, Range, X-Admin-Key, Authorization',
      'Access-Control-Max-Age': '86400',
      'Vary': 'Origin'
    };
    if (origin) {
      headers['Access-Control-Allow-Origin'] = origin;
      headers['Access-Control-Allow-Credentials'] = 'true';
    }
    res.writeHead(204, headers);
    return res.end();
  }

  // 2. Chống CSRF: Từ chối các POST/PUT/DELETE từ Origin độc hại bên ngoài
  if (['POST', 'PUT', 'DELETE'].includes(req.method) && origin && !isOriginAllowed(origin)) {
    return sendJson(res, 403, { ok: false, error: 'Forbidden: Yêu cầu vi phạm chính sách CORS/CSRF' }, origin);
  }

  // 3. Rate Limiting phòng chống Flood DoS
  const isDestructive = ['/api/delete', '/api/empty-trash', '/api/rename', '/api/telegram'].includes(req.url.split('?')[0]);
  if (!checkRateLimit(req, isDestructive)) {
    res.writeHead(429, {
      'Content-Type': 'application/json; charset=utf-8',
      'Retry-After': '10'
    });
    return res.end(JSON.stringify({ ok: false, error: 'Quá nhiều yêu cầu, vui lòng thử lại sau vài giây' }));
  }

  const hostHeader = req.headers.host || `localhost:${PORT}`;
  let url;
  try {
    url = new URL(req.url, `http://${hostHeader}`);
  } catch (_) {
    res.writeHead(400, { 'Content-Type': 'text/plain; charset=utf-8' });
    return res.end('Bad URL Request');
  }

  let pathname = url.pathname;

  // Hỗ trợ redirect /drive -> /
  if (pathname === '/drive/' || pathname === '/drive/index.html') {
    pathname = '/';
  } else if (pathname === '/drive') {
    res.writeHead(301, { Location: '/' });
    return res.end();
  }

  // =========================================================================
  // API AUTHENTICATION ROUTES
  // =========================================================================

  // POST /api/auth/login
  if (pathname === '/api/auth/login' && req.method === 'POST') {
    const ip = getClientIp(req);
    const attempt = checkLoginAttempt(ip);
    if (!attempt.allowed) {
      return sendJson(res, 429, { ok: false, error: attempt.error }, origin);
    }

    try {
      const body = await readJsonBody(req, res, 16 * 1024);
      const username = String(body.username || '').trim();
      const password = String(body.password || '');

      if (!username || !password) {
        return sendJson(res, 400, { ok: false, error: 'Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu' }, origin);
      }

      usersList = loadUsers();
      const user = usersList.find(u => u.username.toLowerCase() === username.toLowerCase());

      if (!user || !verifyPassword(password, user.password_hash)) {
        recordLoginFailed(ip);
        return sendJson(res, 401, { ok: false, error: 'Tên đăng nhập hoặc mật khẩu không chính xác' }, origin);
      }

      recordLoginSuccess(ip);
      const session = createSession(user);

      // Thiết lập Cookie HttpOnly bảo mật cho phiên làm việc
      const cookieHeader = `nas_session=${session.token}; Path=/; HttpOnly; SameSite=Lax; Max-Age=${Math.floor(SESSION_TTL_MS / 1000)}`;
      res.setHeader('Set-Cookie', cookieHeader);

      return sendJson(res, 200, {
        ok: true,
        token: session.token,
        user: session.user,
        message: 'Đăng nhập thành công'
      }, origin);
    } catch (err) {
      return sendJson(res, 500, { ok: false, error: 'Lỗi xử lý đăng nhập' }, origin);
    }
  }

  // POST /api/auth/logout
  if (pathname === '/api/auth/logout' && req.method === 'POST') {
    let token = '';
    const authHeader = req.headers.authorization;
    if (authHeader && authHeader.toLowerCase().startsWith('bearer ')) {
      token = authHeader.slice(7).trim();
    }
    if (!token) {
      const cookies = parseCookies(req);
      token = cookies.nas_session;
    }
    if (!token) {
      token = url.searchParams.get('token') || '';
    }

    if (token) {
      sessions.delete(token);
      saveSessions();
    }

    res.setHeader('Set-Cookie', 'nas_session=; Path=/; HttpOnly; SameSite=Lax; Max-Age=0');
    return sendJson(res, 200, { ok: true, message: 'Đã đăng xuất' }, origin);
  }

  // GET /api/auth/me
  if (pathname === '/api/auth/me' && req.method === 'GET') {
    const user = getAuthUser(req, url);
    if (!user) {
      return sendJson(res, 401, {
        ok: false,
        error: 'Chưa đăng nhập hoặc phiên đã hết hạn',
        code: 'UNAUTHORIZED'
      }, origin);
    }
    return sendJson(res, 200, { ok: true, user }, origin);
  }

  // POST /api/auth/change-password
  if (pathname === '/api/auth/change-password' && req.method === 'POST') {
    const authUser = getAuthUser(req, url);
    if (!authUser) {
      return sendJson(res, 401, { ok: false, error: 'Chưa đăng nhập', code: 'UNAUTHORIZED' }, origin);
    }

    try {
      const body = await readJsonBody(req, res, 16 * 1024);
      const oldPassword = String(body.oldPassword || '');
      const newPassword = String(body.newPassword || '');

      if (!oldPassword || !newPassword) {
        return sendJson(res, 400, { ok: false, error: 'Vui lòng nhập mật khẩu hiện tại và mật khẩu mới' }, origin);
      }
      if (newPassword.length < 6) {
        return sendJson(res, 400, { ok: false, error: 'Mật khẩu mới phải từ 6 ký tự trở lên' }, origin);
      }

      usersList = loadUsers();
      const user = usersList.find(u => u.id === authUser.id || u.username === authUser.username);
      if (!user) {
        return sendJson(res, 404, { ok: false, error: 'Không tìm thấy thông tin tài khoản' }, origin);
      }

      if (!verifyPassword(oldPassword, user.password_hash)) {
        return sendJson(res, 400, { ok: false, error: 'Mật khẩu hiện tại không đúng' }, origin);
      }

      user.password_hash = hashPassword(newPassword);
      user.updated_at = new Date().toISOString();
      saveUsers(usersList);

      return sendJson(res, 200, { ok: true, message: 'Đổi mật khẩu thành công' }, origin);
    } catch (err) {
      return sendJson(res, 500, { ok: false, error: 'Lỗi đổi mật khẩu: ' + err.message }, origin);
    }
  }

  // =========================================================================
  // KIỂM TRA XÁC THỰC TOÀN BỘ CÁC API DỮ LIỆU CÒN LẠI
  // =========================================================================
  let authUser = null;
  if (pathname.startsWith('/api/')) {
    authUser = getAuthUser(req, url);
    if (!authUser) {
      if (pathname === '/api/stream' || pathname === '/api/thumbnail' || pathname === '/api/download' || pathname.startsWith('/api/vault/stream') || pathname.startsWith('/api/vault/download')) {
        res.writeHead(401, {
          'Content-Type': 'text/plain; charset=utf-8',
          'WWW-Authenticate': 'Bearer realm="NAS Drive"'
        });
        return res.end('401 Unauthorized: Vui lòng đăng nhập để truy cập tài nguyên này');
      }
      return sendJson(res, 401, {
        ok: false,
        error: 'Chưa đăng nhập hoặc phiên làm việc đã hết hạn',
        code: 'UNAUTHORIZED'
      }, origin);
    }
  }

  // =========================================================================
  // VAULT (KÉT SẮT BẢO MẬT AES-256-GCM) & FILE CRYPTO ROUTES
  // =========================================================================

  // GET /api/vault/status
  if (pathname === '/api/vault/status' && req.method === 'GET') {
    const cfg = getVaultConfig();
    const key = getActiveVaultKey(authUser.id);
    return sendJson(res, 200, {
      ok: true,
      isConfigured: !!(cfg && cfg.isConfigured),
      isUnlocked: !!key
    }, origin);
  }

  // POST /api/vault/setup
  if (pathname === '/api/vault/setup' && req.method === 'POST') {
    const cfg = getVaultConfig();
    if (cfg && cfg.isConfigured) {
      return sendJson(res, 400, { ok: false, error: 'Két sắt đã được thiết lập trước đó' }, origin);
    }
    try {
      const body = await readJsonBody(req, res, 16 * 1024);
      const password = String(body.password || '');
      if (!password || password.length < 6) {
        return sendJson(res, 400, { ok: false, error: 'Mật khẩu Két sắt phải từ 6 ký tự trở lên' }, origin);
      }
      const salt = crypto.randomBytes(16).toString('hex');
      const key = deriveVaultKey(password, salt);
      const verifier = crypto.createHash('sha256').update(key).digest('hex');

      saveVaultConfig({
        isConfigured: true,
        salt,
        verifier,
        createdAt: new Date().toISOString()
      });

      vaultSessions.set(authUser.id, { key, expiresAt: Date.now() + VAULT_TTL_MS });
      return sendJson(res, 200, { ok: true, message: 'Đã thiết lập Két sắt thành công' }, origin);
    } catch (err) {
      return sendJson(res, 500, { ok: false, error: 'Lỗi thiết lập Két sắt: ' + err.message }, origin);
    }
  }

  // POST /api/vault/unlock
  if (pathname === '/api/vault/unlock' && req.method === 'POST') {
    const cfg = getVaultConfig();
    if (!cfg || !cfg.isConfigured) {
      return sendJson(res, 400, { ok: false, error: 'Két sắt chưa được thiết lập mật khẩu' }, origin);
    }
    try {
      const body = await readJsonBody(req, res, 16 * 1024);
      const password = String(body.password || '');
      if (!password) {
        return sendJson(res, 400, { ok: false, error: 'Vui lòng nhập mật khẩu Két sắt' }, origin);
      }
      const key = deriveVaultKey(password, cfg.salt);
      const testHash = crypto.createHash('sha256').update(key).digest('hex');
      if (!safeCompare(testHash, cfg.verifier)) {
        return sendJson(res, 401, { ok: false, error: 'Mật khẩu Két sắt không chính xác' }, origin);
      }

      vaultSessions.set(authUser.id, { key, expiresAt: Date.now() + VAULT_TTL_MS });
      startBackgroundMigration(key);
      return sendJson(res, 200, { ok: true, message: 'Đã mở khóa Thư mục bí mật' }, origin);
    } catch (err) {
      return sendJson(res, 500, { ok: false, error: 'Lỗi mở khóa: ' + err.message }, origin);
    }
  }

  // POST /api/vault/lock
  if (pathname === '/api/vault/lock' && req.method === 'POST') {
    vaultSessions.delete(authUser.id);
    return sendJson(res, 200, { ok: true, message: 'Đã khóa Thư mục bí mật' }, origin);
  }

  // POST /api/vault/change-password
  if (pathname === '/api/vault/change-password' && req.method === 'POST') {
    const cfg = getVaultConfig();
    if (!cfg || !cfg.isConfigured) {
      return sendJson(res, 400, { ok: false, error: 'Thư mục bí mật chưa được thiết lập' }, origin);
    }
    try {
      const body = await readJsonBody(req, res, 16 * 1024);
      const oldPassword = String(body.oldPassword || '');
      const newPassword = String(body.newPassword || '');

      if (!oldPassword || !newPassword || newPassword.length < 6) {
        return sendJson(res, 400, { ok: false, error: 'Mật khẩu mới phải từ 6 ký tự trở lên' }, origin);
      }

      const oldKey = deriveVaultKey(oldPassword, cfg.salt);
      const testHash = crypto.createHash('sha256').update(oldKey).digest('hex');
      if (!safeCompare(testHash, cfg.verifier)) {
        return sendJson(res, 401, { ok: false, error: 'Mật khẩu Thư mục bí mật hiện tại không chính xác' }, origin);
      }

      const newSalt = crypto.randomBytes(16).toString('hex');
      const newKey = deriveVaultKey(newPassword, newSalt);
      const newVerifier = crypto.createHash('sha256').update(newKey).digest('hex');

      // Tái mã hóa các tệp legacy còn lại nếu có
      if (fs.existsSync(VAULT_ITEMS_DIR)) {
        const itemFiles = fs.readdirSync(VAULT_ITEMS_DIR);
        for (const f of itemFiles) {
          if (!f.endsWith('.enc')) continue;
          const fpath = path.join(VAULT_ITEMS_DIR, f);
          try {
            const raw = fs.readFileSync(fpath);
            const decrypted = decryptVaultFile(raw, oldKey);
            const reEncrypted = encryptVaultFile(decrypted.payload, decrypted.meta, newKey);
            fs.writeFileSync(fpath, reEncrypted);
          } catch (e) {
            console.error(`[Vault] Lỗi tái mã hóa tệp ${f}:`, e.message);
          }
        }
      }

      vaultMetadataCache.clear();
      saveVaultConfig({
        isConfigured: true,
        salt: newSalt,
        verifier: newVerifier,
        updatedAt: new Date().toISOString()
      });

      vaultSessions.set(authUser.id, { key: newKey, expiresAt: Date.now() + VAULT_TTL_MS });
      startBackgroundMigration(newKey);
      return sendJson(res, 200, { ok: true, message: 'Đổi mật khẩu Thư mục bí mật thành công' }, origin);
    } catch (err) {
      return sendJson(res, 500, { ok: false, error: 'Lỗi đổi mật khẩu: ' + err.message }, origin);
    }
  }

  // GET /api/vault/files (Duyệt tệp & thư mục trong Thư mục khóa ẩn)
  if (pathname === '/api/vault/files' && req.method === 'GET') {
    const key = getActiveVaultKey(authUser.id);
    if (!key) {
      return sendJson(res, 403, { ok: false, error: 'Thư mục bí mật đang khóa. Vui lòng mở khóa để xem tệp.', code: 'VAULT_LOCKED' }, origin);
    }

    try {
      const folderParam = url.searchParams.get('folder') || '';
      const targetDir = folderParam ? safeVaultResolve(folderParam, { mustExist: true }) : VAULT_STORAGE_DIR;
      let targetDirStat;
      try { targetDirStat = targetDir ? fs.statSync(targetDir) : null; } catch (_) { targetDirStat = null; }
      if (!targetDir || !targetDirStat || !targetDirStat.isDirectory()) {
        return sendJson(res, 404, { ok: false, error: 'Thư mục không tồn tại' }, origin);
      }

      const items = [];
      const entries = fs.readdirSync(targetDir, { withFileTypes: true });

      for (const entry of entries) {
        if (entry.name.startsWith('.')) continue;
        const fullPath = path.join(targetDir, entry.name);
        const rel = path.relative(VAULT_STORAGE_DIR, fullPath).replace(/\\/g, '/');
        try {
          const st = fs.statSync(fullPath);
          const isDir = entry.isDirectory();
          const ext = path.extname(entry.name).toLowerCase();
          items.push({
            id: rel,
            path: rel,
            name: entry.name,
            folder: folderParam,
            isFolder: isDir,
            size: isDir ? 0 : st.size,
            formattedSize: isDir ? '--' : formatBytes(st.size),
            mimeType: isDir ? 'directory' : (mimeTypes[ext] || 'application/octet-stream'),
            createdAt: (st.birthtime || st.mtime || new Date()).toISOString(),
            type: isDir ? 'folder' : getFileType(entry.name, false),
            isVault: true
          });
        } catch (_) {}
      }

      // Kèm theo các tệp mã hóa cũ nếu đang ở thư mục gốc (tối ưu không làm block event loop)
      if (!folderParam || folderParam === '/' || folderParam === '.') {
        if (fs.existsSync(VAULT_ITEMS_DIR)) {
          const files = fs.readdirSync(VAULT_ITEMS_DIR);
          let diskReadCount = 0;
          for (const f of files) {
            if (!f.endsWith('.enc')) continue;
            const fpath = path.join(VAULT_ITEMS_DIR, f);
            try {
              let meta = null;
              const cached = vaultMetadataCache.get(fpath);
              if (cached && cached.meta) {
                meta = cached.meta;
              } else if (diskReadCount < 80) {
                meta = readVaultMetadataFromFile(fpath, key);
                diskReadCount++;
              }
              if (meta) {
                const id = meta.id || path.basename(f, '.enc');
                if (!items.some(i => i.name === (meta.name || f))) {
                  items.push({
                    id,
                    path: id,
                    name: meta.name || f,
                    folder: meta.folder || '',
                    isFolder: false,
                    size: meta.size || 0,
                    formattedSize: formatBytes(meta.size || 0),
                    mimeType: meta.mimeType || 'application/octet-stream',
                    createdAt: meta.createdAt || new Date().toISOString(),
                    type: getFileType(meta.name || f, false),
                    isVault: true,
                    isLegacyEnc: true
                  });
                }
              }
            } catch (_) {}
          }
        }
      }

      items.sort((a, b) => {
        if (a.isFolder && !b.isFolder) return -1;
        if (!a.isFolder && b.isFolder) return 1;
        return a.name.localeCompare(b.name, undefined, { numeric: true, sensitivity: 'base' });
      });

      return sendJson(res, 200, { ok: true, items }, origin);
    } catch (err) {
      return sendJson(res, 500, { ok: false, error: 'Lỗi đọc danh sách Thư mục bí mật: ' + err.message }, origin);
    }
  }

  // POST /api/vault/import (Chuyển tức thì tệp & thư mục vào Thư mục khóa ẩn - 0.001s)
  if (pathname === '/api/vault/import' && req.method === 'POST') {
    const key = getActiveVaultKey(authUser.id);
    if (!key) {
      return sendJson(res, 403, { ok: false, error: 'Thư mục bí mật đang khóa. Vui lòng mở khóa để chuyển tệp.', code: 'VAULT_LOCKED' }, origin);
    }
    try {
      const body = await readJsonBody(req, res, 64 * 1024);
      const targetPaths = Array.isArray(body.paths) ? body.paths : (body.path ? [body.path] : []);
      if (targetPaths.length === 0) {
        return sendJson(res, 400, { ok: false, error: 'Không có tệp hoặc thư mục nào được chọn' }, origin);
      }

      const moved = [];
      const errors = [];

      for (const targetRelPath of targetPaths) {
        const absPath = safeResolve(String(targetRelPath), { mustExist: true });
        if (!absPath) {
          errors.push(`Không tìm thấy: ${targetRelPath}`);
          continue;
        }

        const baseName = path.basename(absPath);
        const { finalPath, releaseReservation } = getSafeUniqueFilePath(VAULT_STORAGE_DIR, baseName);

        try {
          // fs.renameSync trên cùng mount ext4 là atomic inode: 1ms cho bất kỳ thư mục hay tệp lớn
          fs.renameSync(absPath, finalPath);
          releaseReservation();
          moved.push({ path: targetRelPath, name: baseName });
        } catch (err) {
          releaseReservation();
          console.error(`[Vault Import] Lỗi di chuyển ${absPath}:`, err.message);
          errors.push(err.message);
        }
      }

      broadcastEvent('file_changed', {});
      return sendJson(res, 200, {
        ok: true,
        count: moved.length,
        total: moved.length,
        imported: moved,
        errors: errors.length > 0 ? errors : undefined,
        message: `Đã chuyển ${moved.length} mục vào Thư mục bí mật thành công`
      }, origin);
    } catch (err) {
      return sendJson(res, 500, { ok: false, error: 'Lỗi chuyển tệp vào Thư mục bí mật: ' + err.message }, origin);
    }
  }

  // POST /api/vault/export (Đưa tệp & thư mục từ Thư mục bí mật về lại Drive - Siêu tốc 0.001s)
  if (pathname === '/api/vault/export' && req.method === 'POST') {
    const key = getActiveVaultKey(authUser.id);
    if (!key) {
      return sendJson(res, 403, { ok: false, error: 'Thư mục bí mật đang khóa', code: 'VAULT_LOCKED' }, origin);
    }
    try {
      const body = await readJsonBody(req, res, 64 * 1024);
      const targetIds = Array.isArray(body.ids) ? body.ids : (body.id ? [body.id] : []);
      const targetFolder = String(body.targetFolder || '');
      const targetDir = targetFolder ? safeResolve(targetFolder, { mustExist: true }) : STORAGE_ROOT;
      if (!targetDir) {
        return sendJson(res, 400, { ok: false, error: 'Thư mục đích không tồn tại' }, origin);
      }
      let targetDirStat;
      try { targetDirStat = fs.statSync(targetDir); } catch (_) { targetDirStat = null; }
      if (!targetDirStat || !targetDirStat.isDirectory()) {
        return sendJson(res, 400, { ok: false, error: 'Thư mục đích không tồn tại' }, origin);
      }

      const exported = [];
      for (const rawId of targetIds) {
        const id = String(rawId || '').trim();
        if (!id) continue;

        // 1. Kiểm tra trong Thư mục khóa ẩn (VAULT_STORAGE_DIR)
        const vaultPath = safeVaultResolve(id, { mustExist: true });
        if (vaultPath && fs.existsSync(vaultPath)) {
          const baseName = path.basename(vaultPath);
          const { finalPath, releaseReservation } = getSafeUniqueFilePath(targetDir, baseName);
          try {
            fs.renameSync(vaultPath, finalPath);
            releaseReservation();
            exported.push({ id, name: baseName });
            continue;
          } catch (e) {
            releaseReservation();
            console.error(`[Vault Export] Lỗi di chuyển ${vaultPath}:`, e.message);
          }
        }

        // 2. Kiểm tra nếu là tệp mã hóa cũ trong VAULT_ITEMS_DIR
        const cleanId = id.replace(/[^a-zA-Z0-9_-]/g, '');
        const encPath = path.join(VAULT_ITEMS_DIR, `${cleanId}.enc`);
        if (fs.existsSync(encPath)) {
          const st = fs.statSync(encPath);
          const meta = readVaultMetadataFromFile(encPath, key);
          if (meta) {
            const { finalPath, releaseReservation } = getSafeUniqueFilePath(targetDir, meta.name);
            fs.mkdirSync(path.dirname(finalPath), { recursive: true });
            if (st.size > 30 * 1024 * 1024) {
              await decryptVaultFileStream(encPath, finalPath, key);
            } else {
              const raw = fs.readFileSync(encPath);
              const decrypted = decryptVaultFile(raw, key);
              fs.writeFileSync(finalPath, decrypted.payload);
            }
            releaseReservation();
            fs.unlinkSync(encPath);
            vaultMetadataCache.delete(encPath);
            exported.push({ id: cleanId, name: meta.name });
          }
        }
      }

      broadcastEvent('file_changed', { path: targetFolder });
      return sendJson(res, 200, {
        ok: true,
        count: exported.length,
        message: `Đã đưa ${exported.length} mục về lại Drive thành công`,
        exported
      }, origin);
    } catch (err) {
      return sendJson(res, 500, { ok: false, error: 'Lỗi xuất tệp khỏi Thư mục bí mật: ' + err.message }, origin);
    }
  }

  // POST /api/vault/upload (Tải trực tiếp vào Thư mục khóa ẩn)
  if (pathname === '/api/vault/upload' && req.method === 'POST') {
    const key = getActiveVaultKey(authUser.id);
    if (!key) {
      return sendJson(res, 403, { ok: false, error: 'Thư mục bí mật đang khóa. Vui lòng mở khóa trước.', code: 'VAULT_LOCKED' }, origin);
    }
    const rawName = url.searchParams.get('name') || 'file.bin';
    const folder = url.searchParams.get('folder') || '';
    const origName = path.basename(decodeURIComponent(rawName));
    const targetDir = folder ? (safeVaultResolve(folder, { mustExist: true }) || VAULT_STORAGE_DIR) : VAULT_STORAGE_DIR;
    const { finalPath, releaseReservation } = getSafeUniqueFilePath(targetDir, origName);

    const writeStream = fs.createWriteStream(finalPath);
    req.pipe(writeStream);
    writeStream.on('finish', () => {
      releaseReservation();
      const st = fs.statSync(finalPath);
      const rel = path.relative(VAULT_STORAGE_DIR, finalPath).replace(/\\/g, '/');
      return sendJson(res, 200, { ok: true, id: rel, path: rel, name: origName, size: st.size, isVault: true }, origin);
    });
    writeStream.on('error', (err) => {
      releaseReservation();
      try { fs.unlinkSync(finalPath); } catch (_) {}
      return sendJson(res, 500, { ok: false, error: 'Lỗi tải lên: ' + err.message }, origin);
    });
    return;
  }

  // GET /api/vault/stream & GET /api/vault/download (Phát Video Range 206 & Tải tệp)
  if ((pathname === '/api/vault/stream' || pathname === '/api/vault/download') && req.method === 'GET') {
    const key = getActiveVaultKey(authUser.id);
    if (!key) {
      res.writeHead(403, { 'Content-Type': 'text/plain; charset=utf-8' });
      return res.end('Thư mục bí mật đang khóa');
    }
    const targetRel = (url.searchParams.get('id') || url.searchParams.get('path') || '').trim();
    if (!targetRel) {
      res.writeHead(400, { 'Content-Type': 'text/plain; charset=utf-8' });
      return res.end('Thiếu tham số tệp');
    }

    // 1. Phục vụ từ Thư mục khóa ẩn (VAULT_STORAGE_DIR)
    const vaultPath = safeVaultResolve(targetRel, { mustExist: true });
    if (vaultPath && fs.existsSync(vaultPath)) {
      const stat = fs.statSync(vaultPath);
      if (stat.isDirectory()) {
        res.writeHead(400, { 'Content-Type': 'text/plain; charset=utf-8' });
        return res.end('Không thể tải hoặc stream thư mục');
      }

      if (pathname === '/api/vault/download') {
        const filename = path.basename(vaultPath);
        const ext = path.extname(filename).toLowerCase();
        const contentType = mimeTypes[ext] || 'application/octet-stream';
        applySecurityHeaders(res, origin);
        const isInline = url.searchParams.get('inline') === '1';
        const disposition = isInline
          ? `inline; filename="${encodeURIComponent(filename)}"; filename*=UTF-8''${encodeURIComponent(filename)}`
          : `attachment; filename="${encodeURIComponent(filename)}"; filename*=UTF-8''${encodeURIComponent(filename)}`;
        const headers = {
          'Content-Type': contentType,
          'Content-Length': stat.size,
          'Content-Disposition': disposition,
          'X-Content-Type-Options': 'nosniff'
        };
        if (!isInline) {
          headers['Content-Security-Policy'] = "default-src 'none'; sandbox";
        }
        res.writeHead(200, headers);
        return fs.createReadStream(vaultPath, { highWaterMark: 1024 * 1024 }).pipe(res);
      }

      // Stream trực tiếp hỗ trợ tua Range 206 mượt mà
      return handleStream(req, res, vaultPath, path.basename(vaultPath));
    }

    // 2. Phục vụ tệp mã hóa cũ từ VAULT_ITEMS_DIR
    const cleanId = targetRel.replace(/[^a-zA-Z0-9_-]/g, '');
    const encPath = path.join(VAULT_ITEMS_DIR, `${cleanId}.enc`);
    if (!fs.existsSync(encPath)) {
      res.writeHead(404, { 'Content-Type': 'text/plain; charset=utf-8' });
      return res.end('Không tìm thấy tệp');
    }

    try {
      const st = fs.statSync(encPath);
      const isDownload = pathname === '/api/vault/download';
      const meta = readVaultMetadataFromFile(encPath, key);
      if (!meta) {
        res.writeHead(400, { 'Content-Type': 'text/plain; charset=utf-8' });
        return res.end('Tệp mã hóa không hợp lệ');
      }
      const filename = meta.name || 'file';
      const disposition = isDownload
        ? `attachment; filename="${encodeURIComponent(filename)}"; filename*=UTF-8''${encodeURIComponent(filename)}`
        : 'inline';

      if (st.size > 50 * 1024 * 1024 && (isDownload || !req.headers.range)) {
        applySecurityHeaders(res, origin);
        res.writeHead(200, {
          'Content-Type': meta.mimeType || 'application/octet-stream',
          'Content-Length': meta.size,
          'Content-Disposition': disposition,
          'Cache-Control': 'private, no-cache',
          'X-Content-Type-Options': 'nosniff'
        });
        const fd = fs.openSync(encPath, 'r');
        const header = Buffer.alloc(37);
        fs.readSync(fd, header, 0, 37, 0);
        const iv = header.subarray(5, 17);
        const authTag = header.subarray(17, 33);
        const metaLen = header.readUInt32BE(33);
        fs.closeSync(fd);

        const decipher = crypto.createDecipheriv('aes-256-gcm', key, iv);
        decipher.setAuthTag(authTag);
        const readStream = fs.createReadStream(encPath, { start: 37 + metaLen, highWaterMark: 1024 * 1024 });
        readStream.pipe(decipher).pipe(res);
        return;
      }

      const raw = fs.readFileSync(encPath);
      const decrypted = decryptVaultFile(raw, key);

      applySecurityHeaders(res, origin);
      const totalSize = decrypted.payload.length;
      const range = req.headers.range;

      if (range && !isDownload) {
        const parts = range.replace(/bytes=/, '').split('-');
        const start = parseInt(parts[0], 10);
        const end = parts[1] ? parseInt(parts[1], 10) : totalSize - 1;

        if (isNaN(start) || isNaN(end) || start >= totalSize || end >= totalSize || start > end) {
          res.writeHead(416, { 'Content-Range': `bytes */${totalSize}` });
          return res.end();
        }

        const chunksize = (end - start) + 1;
        res.writeHead(206, {
          'Content-Type': decrypted.meta.mimeType || 'application/octet-stream',
          'Content-Range': `bytes ${start}-${end}/${totalSize}`,
          'Content-Length': chunksize,
          'Accept-Ranges': 'bytes',
          'Content-Disposition': disposition,
          'Cache-Control': 'private, no-cache',
          'X-Content-Type-Options': 'nosniff'
        });
        return res.end(decrypted.payload.subarray(start, end + 1));
      }

      res.writeHead(200, {
        'Content-Type': decrypted.meta.mimeType || 'application/octet-stream',
        'Content-Length': totalSize,
        'Accept-Ranges': 'bytes',
        'Content-Disposition': disposition,
        'Cache-Control': 'private, no-cache',
        'X-Content-Type-Options': 'nosniff'
      });
      return res.end(decrypted.payload);
    } catch (err) {
      res.writeHead(500, { 'Content-Type': 'text/plain; charset=utf-8' });
      return res.end('Lỗi giải mã tệp: ' + err.message);
    }
  }

  // POST /api/vault/delete
  if (pathname === '/api/vault/delete' && req.method === 'POST') {
    const key = getActiveVaultKey(authUser.id);
    if (!key) {
      return sendJson(res, 403, { ok: false, error: 'Thư mục bí mật đang khóa', code: 'VAULT_LOCKED' }, origin);
    }
    try {
      const body = await readJsonBody(req, res, 16 * 1024);
      const rawId = String(body.id || body.path || '').trim();
      if (!rawId) {
        return sendJson(res, 400, { ok: false, error: 'Thiếu ID tệp cần xóa' }, origin);
      }

      // 1. Kiểm tra trong Thư mục khóa ẩn (VAULT_STORAGE_DIR)
      const vaultPath = safeVaultResolve(rawId, { mustExist: true });
      if (vaultPath && fs.existsSync(vaultPath)) {
        fs.rmSync(vaultPath, { recursive: true, force: true });
        return sendJson(res, 200, { ok: true, message: 'Đã xóa vĩnh viễn khỏi Thư mục bí mật' }, origin);
      }

      // 2. Kiểm tra legacy .enc
      const cleanId = rawId.replace(/[^a-zA-Z0-9_-]/g, '');
      const encPath = path.join(VAULT_ITEMS_DIR, `${cleanId}.enc`);
      if (fs.existsSync(encPath)) {
        fs.unlinkSync(encPath);
        vaultMetadataCache.delete(encPath);
        return sendJson(res, 200, { ok: true, message: 'Đã xóa vĩnh viễn khỏi Thư mục bí mật' }, origin);
      }

      return sendJson(res, 404, { ok: false, error: 'Không tìm thấy mục cần xóa' }, origin);
    } catch (err) {
      return sendJson(res, 500, { ok: false, error: 'Lỗi xóa mục: ' + err.message }, origin);
    }
  }

  // POST /api/crypto/encrypt-file (Mã hóa trực tiếp tệp trên Drive)
  if (pathname === '/api/crypto/encrypt-file' && req.method === 'POST') {
    try {
      const body = await readJsonBody(req, res, 16 * 1024);
      const filePath = String(body.path || '');
      const pass = String(body.password || '');
      if (!pass || pass.length < 6) {
        return sendJson(res, 400, { ok: false, error: 'Mật khẩu bảo vệ tệp phải từ 6 ký tự trở lên' }, origin);
      }
      const abs = safeResolve(filePath, { mustExist: true });
      if (!abs || fs.statSync(abs).isDirectory()) {
        return sendJson(res, 400, { ok: false, error: 'Tệp không hợp lệ' }, origin);
      }

      const fileBuf = fs.readFileSync(abs);
      const salt = crypto.randomBytes(16).toString('hex');
      const key = deriveVaultKey(pass, salt);
      const meta = { name: path.basename(abs), size: fileBuf.length, salt };
      const enc = encryptVaultFile(fileBuf, meta, key);

      const finalBuf = Buffer.concat([Buffer.from(salt, 'hex'), enc]);
      const encPath = abs + '.enc';
      fs.writeFileSync(encPath, finalBuf);
      fs.unlinkSync(abs);

      broadcastEvent('file_changed', { path: path.dirname(filePath) });
      return sendJson(res, 200, { ok: true, message: 'Đã mã hóa tệp thành công' }, origin);
    } catch (err) {
      return sendJson(res, 500, { ok: false, error: 'Lỗi mã hóa tệp: ' + err.message }, origin);
    }
  }

  // POST /api/crypto/decrypt-file (Giải mã tệp trên Drive)
  if (pathname === '/api/crypto/decrypt-file' && req.method === 'POST') {
    try {
      const body = await readJsonBody(req, res, 16 * 1024);
      const filePath = String(body.path || '');
      const pass = String(body.password || '');
      if (!pass) {
        return sendJson(res, 400, { ok: false, error: 'Vui lòng nhập mật khẩu giải mã' }, origin);
      }
      const abs = safeResolve(filePath, { mustExist: true });
      if (!abs || !abs.endsWith('.enc')) {
        return sendJson(res, 400, { ok: false, error: 'Tệp không phải là tệp mã hóa .enc' }, origin);
      }

      const raw = fs.readFileSync(abs);
      if (raw.length < 16 + 37) {
        return sendJson(res, 400, { ok: false, error: 'Tệp mã hóa không hợp lệ' }, origin);
      }
      const salt = raw.subarray(0, 16).toString('hex');
      const enc = raw.subarray(16);
      const key = deriveVaultKey(pass, salt);

      const decrypted = decryptVaultFile(enc, key);
      const originalPath = abs.replace(/\.enc$/, '');
      const { finalPath: decFinalPath, releaseReservation: decRelease } = getSafeUniqueFilePath(path.dirname(abs), path.basename(originalPath));
      fs.writeFileSync(decFinalPath, decrypted.payload);
      decRelease();
      fs.unlinkSync(abs);

      broadcastEvent('file_changed', { path: path.dirname(filePath) });
      return sendJson(res, 200, { ok: true, message: 'Đã giải mã tệp thành công' }, origin);
    } catch (err) {
      return sendJson(res, 400, { ok: false, error: 'Mật khẩu giải mã không đúng hoặc tệp bị lỗi' }, origin);
    }
  }

  // API: Luồng sự kiện Realtime SSE
  if (pathname === '/api/events') {
    const sseHeaders = {
      'Content-Type': 'text/event-stream; charset=utf-8',
      'Cache-Control': 'no-cache, no-transform',
      'Connection': 'keep-alive',
      'X-Accel-Buffering': 'no'
    };
    if (origin && isOriginAllowed(origin)) {
      sseHeaders['Access-Control-Allow-Origin'] = origin;
      sseHeaders['Access-Control-Allow-Credentials'] = 'true';
    }
    res.writeHead(200, sseHeaders);
    res.write(':\n\n');
    sseClients.add(res);

    const keepAlive = setInterval(() => {
      try {
        res.write(':\n\n');
      } catch (_) {
        clearInterval(keepAlive);
        sseClients.delete(res);
      }
    }, 25000);

    req.on('close', () => {
      clearInterval(keepAlive);
      sseClients.delete(res);
    });
    return;
  }

  // API: Thông tin dung lượng ổ đĩa NAS
  if (pathname === '/api/storage') {
    try {
      const statFs = fs.statfsSync(STORAGE_ROOT);
      const total = statFs.blocks * statFs.bsize;
      const free = statFs.bfree * statFs.bsize;
      const used = total - free;
      return sendJson(res, 200, {
        ok: true,
        totalBytes: total,
        usedBytes: used,
        freeBytes: free,
        totalGb: (total / (1024**3)).toFixed(1),
        usedGb: (used / (1024**3)).toFixed(1),
        freeGb: (free / (1024**3)).toFixed(1),
        usedPercent: Math.min(100, Math.max(0, Math.round((used / total) * 100)))
      }, origin);
    } catch (err) {
      return sendJson(res, 500, { ok: false, error: err.message }, origin);
    }
  }

  // API: Quản trị Telegram Bot (Được bảo vệ bằng Admin Auth)
  if (pathname === '/api/telegram' && req.method === 'GET') {
    const auth = checkAdminAuth(req, url);
    if (!auth.ok) {
      return sendJson(res, 401, {
        ok: false,
        error: 'Truy cập bị từ chối: Cần cung cấp Khóa quản trị (X-Admin-Key) khi truy cập từ ngoài Internet'
      }, origin);
    }

    const tgConfigPath = path.join(__dirname, 'telegram-config.json');
    let tgConfig = {};
    if (fs.existsSync(tgConfigPath)) {
      try { tgConfig = JSON.parse(fs.readFileSync(tgConfigPath, 'utf8')); } catch (_) {}
    }
    let isRunning = false;
    try {
      isRunning = execSync('systemctl is-active nas-telegram-bot.service', { encoding: 'utf8' }).trim() === 'active';
    } catch (_) {}

    return sendJson(res, 200, {
      ok: true,
      configured: Boolean(tgConfig.botToken),
      botTokenMasked: tgConfig.botToken ? `${tgConfig.botToken.slice(0, 6)}...${tgConfig.botToken.slice(-4)}` : '',
      chatId: tgConfig.chatId || '',
      isRunning,
      alertDiskPercent: tgConfig.alertDiskPercent || 90,
      enablePeriodicReport: tgConfig.enablePeriodicReport !== false,
      adminKey: auth.isLan ? ADMIN_SECRET : undefined
    }, origin);
  }

  if (pathname === '/api/telegram' && req.method === 'POST') {
    const auth = checkAdminAuth(req, url);
    if (!auth.ok) {
      return sendJson(res, 401, {
        ok: false,
        error: 'Truy cập bị từ chối: Cần cung cấp Khóa quản trị (X-Admin-Key) khi cập nhật qua Internet'
      }, origin);
    }

    try {
      const payload = await readJsonBody(req, res);
      const tgConfigPath = path.join(__dirname, 'telegram-config.json');
      let current = {};
      if (fs.existsSync(tgConfigPath)) {
        try { current = JSON.parse(fs.readFileSync(tgConfigPath, 'utf8')); } catch (_) {}
      }
      if (payload.botToken !== undefined) current.botToken = String(payload.botToken).trim();
      if (payload.chatId !== undefined) current.chatId = String(payload.chatId).trim();
      if (payload.alertDiskPercent !== undefined) {
        const p = Number(payload.alertDiskPercent);
        if (!isNaN(p) && p >= 10 && p <= 99) current.alertDiskPercent = p;
      }
      if (payload.enablePeriodicReport !== undefined) current.enablePeriodicReport = Boolean(payload.enablePeriodicReport);

      fs.writeFileSync(tgConfigPath, JSON.stringify(current, null, 2), 'utf8');

      try {
        execSync('sudo systemctl restart nas-telegram-bot.service');
      } catch (_) {}

      return sendJson(res, 200, { ok: true, message: 'Đã cập nhật cấu hình Telegram Bot thành công' }, origin);
    } catch (err) {
      const code = err.message === 'PAYLOAD_TOO_LARGE' ? 413 : 400;
      return sendJson(res, code, { ok: false, error: err.message }, origin);
    }
  }

  // API: Liệt kê danh sách tệp & tìm kiếm (Tối ưu hóa đa luồng & Bộ nhớ đệm Cache 0ms)
  if (pathname === '/api/files' && req.method === 'GET') {
    const folder = url.searchParams.get('folder') || '';
    const filter = url.searchParams.get('filter') || 'all';
    const q = (url.searchParams.get('q') || '').trim().toLowerCase();
    const isTrashView = url.searchParams.get('trash') === '1';

    let targetDir = isTrashView ? TRASH_DIR : safeResolve(folder, { mustExist: true });
    if (!targetDir || !fs.existsSync(targetDir)) {
      return sendJson(res, 404, { ok: false, error: 'Thư mục không tồn tại' }, origin);
    }

    // 1. Kiểm tra cache trong RAM (phản hồi ngay trong 0ms nếu đã duyệt gần đây)
    const cacheKey = `${isTrashView ? 'trash' : folder}|${filter}|${q}`;
    const cached = folderListCache.get(cacheKey);
    if (cached && (Date.now() - cached.time < FOLDER_CACHE_TTL_MS)) {
      return sendJson(res, 200, {
        ok: true,
        currentFolder: folder,
        isTrash: isTrashView,
        items: cached.items,
        cached: true
      }, origin);
    }

    try {
      let items = [];

      // 2. Nếu có tìm kiếm từ khóa q: quét đệ quy từ targetDir xuống
      if (q) {
        const searchRoot = targetDir;
        const allItems = await scanDirRecursive(searchRoot, 3);
        items = allItems.filter(item => {
          if (filter !== 'all' && item.type !== filter) return false;
          if (!item.name.toLowerCase().includes(q)) return false;
          return true;
        });
      }
      // 3. Nếu ở gốc (folder rỗng) và đang chọn bộ lọc danh mục (Video, Ảnh, v.v.): quét đệ quy từ gốc
      else if (!folder && !isTrashView && filter !== 'all') {
        const allItems = await scanDirRecursive(STORAGE_ROOT, 3);
        items = allItems.filter(item => item.type === filter);
      }
      // 4. Mở thư mục thông thường: Đọc trực tiếp targetDir SONG SONG (cực nhanh, không đệ quy thừa)
      else {
        const dirents = await fs.promises.readdir(targetDir, { withFileTypes: true });
        const valid = dirents.filter(ent => !ent.name.startsWith('.'));

        const rawItems = await Promise.all(valid.map(async ent => {
          const fullPath = path.join(targetDir, ent.name);
          const isFolder = ent.isDirectory();
          const relPath = path.relative(STORAGE_ROOT, fullPath).replace(/\\/g, '/');
          let size = 0;
          let mtime = 0;
          try {
            const st = await fs.promises.stat(fullPath);
            size = st.size;
            mtime = st.mtimeMs;
          } catch (_) {}

          return {
            name: ent.name,
            path: relPath,
            isFolder,
            size,
            formattedSize: isFolder ? '--' : formatBytes(size),
            mtime,
            formattedDate: dateFormatter.format(mtime),
            type: getFileType(ent.name, isFolder),
            ext: path.extname(ent.name).toLowerCase()
          };
        }));

        if (filter !== 'all') {
          items = rawItems.filter(item => item.isFolder || item.type === filter);
        } else {
          items = rawItems;
        }
      }

      items.sort((a, b) => {
        if (a.isFolder && !b.isFolder) return -1;
        if (!a.isFolder && b.isFolder) return 1;
        return nameCollator.compare(a.name, b.name);
      });

      // Ghi nhớ vào cache
      folderListCache.set(cacheKey, { items, time: Date.now() });
      if (folderListCache.size > 200) {
        const oldestKey = folderListCache.keys().next().value;
        folderListCache.delete(oldestKey);
      }

      return sendJson(res, 200, {
        ok: true,
        currentFolder: folder,
        isTrash: isTrashView,
        items
      }, origin);
    } catch (err) {
      return sendJson(res, 500, { ok: false, error: err.message }, origin);
    }
  }

  // API: Tạo thư mục mới
  if (pathname === '/api/folder' && req.method === 'POST') {
    try {
      const data = await readJsonBody(req, res);
      const { folder = '', name = '', relPath = '', isVault = false } = data;
      let rootDir = STORAGE_ROOT;
      let parentDir;
      if (isVault) {
        const key = getActiveVaultKey(authUser?.id);
        if (!key) {
          return sendJson(res, 403, { ok: false, error: 'Thư mục bí mật đang khóa', code: 'VAULT_LOCKED' }, origin);
        }
        rootDir = VAULT_STORAGE_DIR;
        parentDir = folder ? safeVaultResolve(folder) : VAULT_STORAGE_DIR;
      } else {
        parentDir = safeResolve(folder);
      }
      if (!parentDir) return sendJson(res, 403, { ok: false, error: 'Đường dẫn thư mục cha không hợp lệ' }, origin);

      const rawName = relPath ? relPath : name;
      if (!rawName || typeof rawName !== 'string' || rawName.includes('\0')) {
        return sendJson(res, 400, { ok: false, error: 'Tên thư mục không hợp lệ' }, origin);
      }

      const cleanSegments = path.normalize(rawName).replace(/\\/g, '/').split('/').filter(Boolean);
      if (cleanSegments.length === 0 || cleanSegments.includes('..')) {
        return sendJson(res, 400, { ok: false, error: 'Tên thư mục chứa ký tự không an toàn' }, origin);
      }

      if (['.trash', '.chunks', '.thumbnails', '.vault'].includes(cleanSegments[0].toLowerCase())) {
        return sendJson(res, 403, { ok: false, error: 'Không thể tạo thư mục hệ thống bảo lưu' }, origin);
      }

      const folderRel = cleanSegments.join(path.sep);
      const targetDir = path.resolve(parentDir, folderRel);
      if (!targetDir.startsWith(rootDir + path.sep)) {
        return sendJson(res, 403, { ok: false, error: 'Phạm vi đường dẫn không hợp lệ' }, origin);
      }

      fs.mkdirSync(targetDir, { recursive: true });
      broadcastEvent('file_changed', { action: 'create_folder' });
      return sendJson(res, 200, {
        ok: true,
        name: path.basename(targetDir),
        path: path.relative(rootDir, targetDir).replace(/\\/g, '/')
      }, origin);
    } catch (err) {
      const code = err.message === 'PAYLOAD_TOO_LARGE' ? 413 : 400;
      return sendJson(res, code, { ok: false, error: err.message }, origin);
    }
  }

  // API: Tải tệp theo từng mảnh (Chunked Upload)
  if (pathname === '/api/upload-chunk' && req.method === 'POST') {
    const folder = url.searchParams.get('folder') || '';
    const rawRelPath = url.searchParams.get('relPath') || url.searchParams.get('name') || '';
    const uploadId = (url.searchParams.get('uploadId') || '').replace(/[^a-zA-Z0-9_-]/g, '');
    const chunkIndex = parseInt(url.searchParams.get('chunkIndex') || '0', 10);
    const totalChunks = parseInt(url.searchParams.get('totalChunks') || '1', 10);

    if (!rawRelPath || !uploadId || uploadId.length < 4 || uploadId.length > 64 ||
        isNaN(chunkIndex) || isNaN(totalChunks) || chunkIndex < 0 || chunkIndex >= totalChunks || totalChunks > 10000) {
      return sendJson(res, 400, { ok: false, error: 'Thông số chunk không hợp lệ' }, origin);
    }

    if (rawRelPath.includes('\0')) {
      return sendJson(res, 400, { ok: false, error: 'Tên file chứa ký tự không hợp lệ' }, origin);
    }

    const cleanRelPath = path.normalize(rawRelPath).replace(/\\/g, '/').replace(/^\/+/, '');
    const parts = cleanRelPath.split('/').filter(Boolean);
    if (parts.includes('..') || parts.length === 0) {
      return sendJson(res, 400, { ok: false, error: 'Đường dẫn tệp không an toàn' }, origin);
    }

    if (['.trash', '.chunks', '.thumbnails'].includes(parts[0].toLowerCase())) {
      return sendJson(res, 403, { ok: false, error: 'Không thể ghi vào thư mục hệ thống' }, origin);
    }

    const parentDir = safeResolve(folder);
    if (!parentDir) return sendJson(res, 403, { ok: false, error: 'Đường dẫn thư mục cha không hợp lệ' }, origin);

    const targetFilePath = path.resolve(parentDir, cleanRelPath);
    if (!targetFilePath.startsWith(STORAGE_ROOT + path.sep)) {
      return sendJson(res, 403, { ok: false, error: 'Đường dẫn vi phạm phạm vi lưu trữ' }, origin);
    }

    const chunkSessionDir = path.join(CHUNKS_DIR, uploadId);
    if (!fs.existsSync(chunkSessionDir)) fs.mkdirSync(chunkSessionDir, { recursive: true });

    const chunkPath = path.join(chunkSessionDir, `part_${String(chunkIndex).padStart(6, '0')}`);
    const writeStream = fs.createWriteStream(chunkPath, { highWaterMark: 1024 * 1024 });

    let writeErrored = false;
    writeStream.on('error', err => {
      writeErrored = true;
      if (!res.headersSent && !res.writableEnded) {
        return sendJson(res, 500, { ok: false, error: err.message }, origin);
      }
    });

    req.pipe(writeStream);

    writeStream.on('finish', async () => {
      if (writeErrored) return;
      try {
        const files = await fs.promises.readdir(chunkSessionDir);
        const parts = files.filter(f => f.startsWith('part_'));

        if (parts.length < totalChunks) {
          return sendJson(res, 200, { ok: true, chunkIndex, received: parts.length, totalChunks, done: false }, origin);
        }

        // Đã nhận đủ tất cả chunks! Ghép lại thành tệp hoàn chỉnh
        parts.sort();
        const targetDir = path.dirname(targetFilePath);
        if (!fs.existsSync(targetDir)) fs.mkdirSync(targetDir, { recursive: true });

        const originalFilename = path.basename(targetFilePath);
        const { finalName, finalPath, tempPath: tempMergedPath, releaseReservation } = getSafeUniqueFilePath(targetDir, originalFilename);
        const mergedStream = fs.createWriteStream(tempMergedPath, { highWaterMark: 4 * 1024 * 1024 });

        let mergeErrorOccurred = false;
        mergedStream.on('error', (err) => {
          mergeErrorOccurred = true;
          releaseReservation();
          try { if (fs.existsSync(tempMergedPath)) fs.unlinkSync(tempMergedPath); } catch (_) {}
          if (!res.headersSent && !res.writableEnded) {
            return sendJson(res, 500, { ok: false, error: 'Lỗi ghi tệp hợp nhất: ' + err.message }, origin);
          }
        });

        for (const part of parts) {
          if (mergeErrorOccurred) return;
          const partPath = path.join(chunkSessionDir, part);
          await new Promise((resolve, reject) => {
            const reader = fs.createReadStream(partPath, { highWaterMark: 1024 * 1024 });
            reader.pipe(mergedStream, { end: false });
            reader.on('end', resolve);
            reader.on('error', reject);
          });
        }
        if (mergeErrorOccurred) return;
        mergedStream.end();

        await new Promise((resolve, reject) => {
          mergedStream.on('finish', resolve);
          mergedStream.on('error', reject);
        });

        fs.renameSync(tempMergedPath, finalPath);
        releaseReservation();
        broadcastEvent('file_changed', { action: 'upload', name: finalName });

        if (getFileType(finalName, false) === 'video') {
          const relPath = path.relative(STORAGE_ROOT, finalPath).replace(/\\/g, '/');
          const thumbKey = crypto.createHash('sha256').update(relPath).digest('hex');
          const thumbFile = path.join(THUMBNAILS_DIR, `${thumbKey}.jpg`);
          generateThumbnail(finalPath, thumbFile).catch(() => {});
        }

        try {
          await fs.promises.rm(chunkSessionDir, { recursive: true, force: true });
        } catch (_) {}

        const stat = fs.statSync(finalPath);
        return sendJson(res, 200, {
          ok: true,
          done: true,
          name: finalName,
          originalName: originalFilename,
          renamed: finalName !== originalFilename,
          size: stat.size,
          path: path.relative(STORAGE_ROOT, finalPath).replace(/\\/g, '/')
        }, origin);
      } catch (err) {
        if (!res.headersSent && !res.writableEnded) {
          return sendJson(res, 500, { ok: false, error: 'Lỗi ghép chunk: ' + err.message }, origin);
        }
      }
    });
    return;
  }

  // API: Tải tệp lên dạng Stream trực tiếp
  if (pathname === '/api/upload-stream' && req.method === 'POST') {
    const folder = url.searchParams.get('folder') || '';
    const rawRelPath = url.searchParams.get('relPath') || url.searchParams.get('name') || '';
    if (!rawRelPath || typeof rawRelPath !== 'string' || rawRelPath.includes('\0')) {
      return sendJson(res, 400, { ok: false, error: 'Đường dẫn tệp không hợp lệ' }, origin);
    }

    const cleanRelPath = path.normalize(rawRelPath).replace(/\\/g, '/').replace(/^\/+/, '');
    const parts = cleanRelPath.split('/').filter(Boolean);
    if (parts.includes('..') || parts.length === 0) {
      return sendJson(res, 400, { ok: false, error: 'Đường dẫn tệp không an toàn' }, origin);
    }

    if (['.trash', '.chunks', '.thumbnails'].includes(parts[0].toLowerCase())) {
      return sendJson(res, 403, { ok: false, error: 'Không thể ghi vào thư mục hệ thống' }, origin);
    }

    const parentDir = safeResolve(folder);
    if (!parentDir) return sendJson(res, 403, { ok: false, error: 'Đường dẫn thư mục không hợp lệ' }, origin);

    const targetFilePath = path.resolve(parentDir, cleanRelPath);
    if (!targetFilePath.startsWith(STORAGE_ROOT + path.sep)) {
      return sendJson(res, 403, { ok: false, error: 'Đường dẫn vi phạm phạm vi lưu trữ' }, origin);
    }

    const targetDir = path.dirname(targetFilePath);
    if (!fs.existsSync(targetDir)) fs.mkdirSync(targetDir, { recursive: true });

    const originalFilename = path.basename(targetFilePath);
    const { finalName, finalPath, tempPath, releaseReservation } = getSafeUniqueFilePath(targetDir, originalFilename);
    const writeStream = fs.createWriteStream(tempPath, { highWaterMark: 8 * 1024 * 1024 });
    let bytesReceived = 0;
    let completedNormally = false;

    if (req.socket) req.socket.setNoDelay(true);

    const cleanup = () => {
      releaseReservation();
      try { if (fs.existsSync(tempPath)) fs.unlinkSync(tempPath); } catch (_) {}
    };

    writeStream.on('error', err => {
      cleanup();
      if (!res.headersSent && !res.writableEnded) {
        return sendJson(res, 500, { ok: false, error: 'Lỗi ghi tệp stream: ' + err.message }, origin);
      }
    });

    req.on('data', chunk => {
      bytesReceived += chunk.length;
    });

    req.on('close', () => {
      if (!completedNormally && !req.complete) {
        writeStream.destroy();
        cleanup();
      }
    });

    req.pipe(writeStream);

    writeStream.on('finish', () => {
      if (!req.complete && bytesReceived === 0) {
        cleanup();
        if (!res.headersSent && !res.writableEnded) {
          return sendJson(res, 400, { ok: false, error: 'Tải tệp bị gián đoạn' }, origin);
        }
        return;
      }
      completedNormally = true;
      try {
        fs.renameSync(tempPath, finalPath);
        releaseReservation();
        broadcastEvent('file_changed', { action: 'upload', name: finalName });

        if (getFileType(finalName, false) === 'video') {
          const relPath = path.relative(STORAGE_ROOT, finalPath).replace(/\\/g, '/');
          const thumbKey = crypto.createHash('sha256').update(relPath).digest('hex');
          const thumbFile = path.join(THUMBNAILS_DIR, `${thumbKey}.jpg`);
          generateThumbnail(finalPath, thumbFile).catch(() => {});
        }

        return sendJson(res, 200, {
          ok: true,
          name: finalName,
          originalName: originalFilename,
          renamed: finalName !== originalFilename,
          size: bytesReceived,
          path: path.relative(STORAGE_ROOT, finalPath).replace(/\\/g, '/')
        }, origin);
      } catch (e) {
        cleanup();
        if (!res.headersSent && !res.writableEnded) {
          return sendJson(res, 500, { ok: false, error: e.message }, origin);
        }
      }
    });
    return;
  }

  // API: Đổi tên tệp / thư mục
  if (pathname === '/api/rename' && req.method === 'POST') {
    try {
      const data = await readJsonBody(req, res);
      const { oldPath = '', newName = '' } = data;
      const safeOld = safeResolve(oldPath, { mustExist: true });
      if (!safeOld || typeof newName !== 'string') {
        return sendJson(res, 400, { ok: false, error: 'Thông số không hợp lệ' }, origin);
      }

      if (safeOld === STORAGE_ROOT || safeOld === TRASH_DIR || safeOld === CHUNKS_DIR || safeOld === THUMBNAILS_DIR) {
        return sendJson(res, 403, { ok: false, error: 'Không thể đổi tên thư mục hệ thống' }, origin);
      }

      const cleanNew = path.basename(newName.trim());
      if (!cleanNew || cleanNew === '.' || cleanNew === '..' || cleanNew.includes('/') || cleanNew.includes('\\') || cleanNew.includes('\0')) {
        return sendJson(res, 400, { ok: false, error: 'Tên mới không hợp lệ' }, origin);
      }

      const safeNew = path.join(path.dirname(safeOld), cleanNew);
      if (fs.existsSync(safeNew)) {
        return sendJson(res, 400, { ok: false, error: 'Tên mới đã tồn tại' }, origin);
      }

      fs.renameSync(safeOld, safeNew);
      broadcastEvent('file_changed', { action: 'rename', newName: cleanNew });
      return sendJson(res, 200, { ok: true, newName: cleanNew }, origin);
    } catch (err) {
      const code = err.message === 'PAYLOAD_TOO_LARGE' ? 413 : 400;
      return sendJson(res, code, { ok: false, error: err.message }, origin);
    }
  }

  // API: Lấy danh sách thư mục (phục vụ bộ chọn thư mục Di chuyển)
  if (pathname === '/api/folders' && req.method === 'GET') {
    try {
      const folderParam = url.searchParams.get('folder') || '';
      const isVault = url.searchParams.get('vault') === '1';

      let targetDir;
      let rootDir;
      if (isVault) {
        const key = getActiveVaultKey(authUser?.id);
        if (!key) {
          return sendJson(res, 403, { ok: false, error: 'Thư mục bí mật đang khóa', code: 'VAULT_LOCKED' }, origin);
        }
        rootDir = VAULT_STORAGE_DIR;
        targetDir = folderParam ? safeVaultResolve(folderParam, { mustExist: true }) : VAULT_STORAGE_DIR;
      } else {
        rootDir = STORAGE_ROOT;
        targetDir = folderParam ? safeResolve(folderParam, { mustExist: true }) : STORAGE_ROOT;
      }

      if (!targetDir || !fs.existsSync(targetDir) || !fs.statSync(targetDir).isDirectory()) {
        return sendJson(res, 404, { ok: false, error: 'Thư mục không tồn tại' }, origin);
      }

      const entries = fs.readdirSync(targetDir, { withFileTypes: true });
      const folders = [];
      for (const ent of entries) {
        if (!ent.isDirectory() || ent.name.startsWith('.')) continue;
        const full = path.join(targetDir, ent.name);
        const rel = path.relative(rootDir, full).replace(/\\/g, '/');
        let subCount = 0;
        try {
          const subs = fs.readdirSync(full, { withFileTypes: true });
          subCount = subs.filter(s => s.isDirectory() && !s.name.startsWith('.')).length;
        } catch (_) {}

        folders.push({
          name: ent.name,
          path: rel,
          subFolderCount: subCount
        });
      }

      folders.sort((a, b) => a.name.localeCompare(b.name, undefined, { numeric: true, sensitivity: 'base' }));

      const currentRel = path.relative(rootDir, targetDir).replace(/\\/g, '/');
      const parentRel = currentRel ? path.dirname(currentRel).replace(/\\/g, '/') : null;

      return sendJson(res, 200, {
        ok: true,
        currentFolder: currentRel === '.' ? '' : currentRel,
        parentFolder: (parentRel === '.' || parentRel === null) ? '' : parentRel,
        folders
      }, origin);
    } catch (err) {
      return sendJson(res, 500, { ok: false, error: err.message }, origin);
    }
  }

  // API: Di chuyển tệp và thư mục qua các thư mục khác nhau
  if (pathname === '/api/move' && req.method === 'POST') {
    try {
      const data = await readJsonBody(req, res);
      const { paths = [], targetFolder = '', isVault = false } = data;
      if (!Array.isArray(paths) || paths.length === 0 || paths.some(p => typeof p !== 'string')) {
        return sendJson(res, 400, { ok: false, error: 'Danh sách tệp cần di chuyển không hợp lệ' }, origin);
      }

      let rootDir;
      let targetDir;
      if (isVault) {
        const key = getActiveVaultKey(authUser?.id);
        if (!key) {
          return sendJson(res, 403, { ok: false, error: 'Thư mục bí mật đang khóa', code: 'VAULT_LOCKED' }, origin);
        }
        rootDir = VAULT_STORAGE_DIR;
        targetDir = targetFolder ? safeVaultResolve(targetFolder, { mustExist: true }) : VAULT_STORAGE_DIR;
      } else {
        rootDir = STORAGE_ROOT;
        targetDir = targetFolder ? safeResolve(targetFolder, { mustExist: true }) : STORAGE_ROOT;
      }

      if (!targetDir || !fs.existsSync(targetDir) || !fs.statSync(targetDir).isDirectory()) {
        return sendJson(res, 400, { ok: false, error: 'Thư mục đích không tồn tại' }, origin);
      }

      const moved = [];
      const errors = [];

      for (const p of paths) {
        const src = isVault
          ? safeVaultResolve(p, { mustExist: true })
          : safeResolve(p, { mustExist: true });

        if (!src) {
          errors.push(`Không tìm thấy: ${p}`);
          continue;
        }

        // Không cho phép di chuyển thư mục gốc hoặc thư mục hệ thống
        if (src === rootDir || src === TRASH_DIR || src === CHUNKS_DIR || src === THUMBNAILS_DIR || src === VAULT_DIR) {
          errors.push(`Không thể di chuyển thư mục hệ thống: ${p}`);
          continue;
        }

        // Chống di chuyển một thư mục vào chính nó hoặc vào thư mục con của nó
        if (targetDir === src || targetDir.startsWith(src + path.sep)) {
          errors.push(`Không thể di chuyển thư mục vào bên trong chính nó: ${p}`);
          continue;
        }

        // Nếu đã ở trong thư mục đích rồi thì bỏ qua
        if (path.dirname(src) === targetDir) {
          continue;
        }

        const baseName = path.basename(src);
        const { finalPath, releaseReservation } = getSafeUniqueFilePath(targetDir, baseName);
        try {
          fs.renameSync(src, finalPath);
          releaseReservation();
          const newRel = path.relative(rootDir, finalPath).replace(/\\/g, '/');
          moved.push({ oldPath: p, newPath: newRel, name: path.basename(finalPath) });
        } catch (e) {
          releaseReservation();
          errors.push(`Lỗi di chuyển ${p}: ${e.message}`);
        }
      }

      broadcastEvent('file_changed', { action: 'move', count: moved.length });
      return sendJson(res, 200, {
        ok: true,
        movedCount: moved.length,
        moved,
        errors: errors.length > 0 ? errors : undefined
      }, origin);
    } catch (err) {
      const code = err.message === 'PAYLOAD_TOO_LARGE' ? 413 : 400;
      return sendJson(res, code, { ok: false, error: err.message }, origin);
    }
  }

  // API: Xóa (Chuyển vào Thùng Rác hoặc Xóa vĩnh viễn)
  if (pathname === '/api/delete' && req.method === 'POST') {
    try {
      const data = await readJsonBody(req, res);
      const { paths = [] } = data;
      if (!Array.isArray(paths) || paths.some(p => typeof p !== 'string')) {
        return sendJson(res, 400, { ok: false, error: 'Danh sách đường dẫn không hợp lệ' }, origin);
      }

      let count = 0;
      for (const p of paths) {
        const src = safeResolve(p, { allowTrash: true, mustExist: true });
        if (src && src !== STORAGE_ROOT && src !== TRASH_DIR && src !== CHUNKS_DIR && src !== THUMBNAILS_DIR) {
          const isInsideTrash = src.startsWith(TRASH_DIR + path.sep);
          if (isInsideTrash) {
            fs.rmSync(src, { recursive: true, force: true });
          } else {
            const baseName = path.basename(src);
            let stamp = Date.now();
            let dest = path.join(TRASH_DIR, `${stamp}_${baseName}`);
            while (fs.existsSync(dest)) dest = path.join(TRASH_DIR, `${++stamp}_${baseName}`);
            fs.renameSync(src, dest);
          }
          count++;
        }
      }
      broadcastEvent('file_changed', { action: 'delete', count });
      return sendJson(res, 200, { ok: true, deletedCount: count }, origin);
    } catch (err) {
      const code = err.message === 'PAYLOAD_TOO_LARGE' ? 413 : 400;
      return sendJson(res, code, { ok: false, error: err.message }, origin);
    }
  }

  // API: Khôi phục từ Thùng Rác
  if (pathname === '/api/restore' && req.method === 'POST') {
    try {
      const data = await readJsonBody(req, res);
      const { paths = [] } = data;
      if (!Array.isArray(paths) || paths.some(p => typeof p !== 'string')) {
        return sendJson(res, 400, { ok: false, error: 'Danh sách đường dẫn không hợp lệ' }, origin);
      }

      let count = 0;
      for (const p of paths) {
        const src = safeResolve(p, { allowTrash: true, mustExist: true });
        if (src && src.startsWith(TRASH_DIR + path.sep)) {
          const rawName = path.basename(src).replace(/^\d+_/, '');
          const cleanName = path.basename(rawName);
          let dest = path.join(STORAGE_ROOT, cleanName);
          const ext = path.extname(cleanName);
          const stem = path.basename(cleanName, ext);
          let n = 1;
          while (fs.existsSync(dest)) {
            dest = path.join(STORAGE_ROOT, `${stem} (${n++})${ext}`);
          }
          fs.renameSync(src, dest);
          count++;
        }
      }
      broadcastEvent('file_changed', { action: 'restore', count });
      return sendJson(res, 200, { ok: true, restoredCount: count }, origin);
    } catch (err) {
      const code = err.message === 'PAYLOAD_TOO_LARGE' ? 413 : 400;
      return sendJson(res, code, { ok: false, error: err.message }, origin);
    }
  }

  // API: Xóa sạch toàn bộ Thùng rác
  if (pathname === '/api/empty-trash' && req.method === 'POST') {
    try {
      if (fs.existsSync(TRASH_DIR)) {
        const entries = fs.readdirSync(TRASH_DIR);
        for (const e of entries) {
          fs.rmSync(path.join(TRASH_DIR, e), { recursive: true, force: true });
        }
      }
      broadcastEvent('file_changed', { action: 'empty_trash' });
      return sendJson(res, 200, { ok: true }, origin);
    } catch (err) {
      return sendJson(res, 500, { ok: false, error: err.message }, origin);
    }
  }

  // API: Phát Video & Audio Streaming (Hỗ trợ tua Range 206)
  if (pathname === '/api/stream') {
    const filePath = url.searchParams.get('path') || '';
    const targetPath = safeResolve(filePath, { mustExist: true });
    if (!targetPath) {
      res.writeHead(404, { 'Content-Type': 'text/plain; charset=utf-8' });
      return res.end('Không tìm thấy tệp');
    }
    const stat = fs.statSync(targetPath);
    if (stat.isDirectory()) {
      res.writeHead(400, { 'Content-Type': 'text/plain; charset=utf-8' });
      return res.end('Không thể stream thư mục');
    }

    const type = getFileType(path.basename(targetPath), false);
    if (type !== 'video' && type !== 'audio') {
      res.writeHead(400, { 'Content-Type': 'text/plain; charset=utf-8' });
      return res.end('Chỉ hỗ trợ stream định dạng video hoặc audio');
    }

    return handleStream(req, res, targetPath, path.basename(targetPath));
  }

  // API: Tạo ảnh xem trước (Thumbnail) cho Video và Ảnh
  if (pathname === '/api/thumbnail') {
    const filePath = url.searchParams.get('path') || '';
    let targetPath = safeResolve(filePath, { mustExist: true });
    if (!targetPath) {
      const isVaultUnlocked = getActiveVaultKey(authUser?.id);
      if (isVaultUnlocked) {
        targetPath = safeVaultResolve(filePath, { mustExist: true });
      }
    }
    if (!targetPath) {
      res.writeHead(404, { 'Content-Type': 'text/plain; charset=utf-8' });
      return res.end('Không tìm thấy tệp');
    }

    const stat = fs.statSync(targetPath);
    if (stat.isDirectory()) {
      res.writeHead(400, { 'Content-Type': 'text/plain; charset=utf-8' });
      return res.end('Không thể tạo thumbnail cho thư mục');
    }

    const ext = path.extname(targetPath).toLowerCase();
    const type = getFileType(path.basename(targetPath), false);

    // Nếu là ảnh thông thường, stream trực tiếp với cache
    if (type === 'image') {
      const contentType = mimeTypes[ext] || 'image/jpeg';
      const imgHeaders = {
        'Content-Type': contentType,
        'Content-Length': stat.size,
        'Cache-Control': 'public, max-age=86400',
        'X-Content-Type-Options': 'nosniff',
        'Content-Security-Policy': "default-src 'none'; sandbox"
      };
      if (origin && isOriginAllowed(origin)) {
        imgHeaders['Access-Control-Allow-Origin'] = origin;
      }
      res.writeHead(200, imgHeaders);
      return fs.createReadStream(targetPath, { highWaterMark: 1024 * 1024 }).pipe(res);
    }

    // Nếu là video, tìm hoặc tạo ảnh thumbnail bằng ffmpeg
    if (type === 'video') {
      const relPath = path.relative(STORAGE_ROOT, targetPath).replace(/\\/g, '/');
      const thumbKey = crypto.createHash('sha256').update(relPath).digest('hex');
      const thumbFile = path.join(THUMBNAILS_DIR, `${thumbKey}.jpg`);

      const serveThumb = () => {
        try {
          const tStat = fs.statSync(thumbFile);
          const thumbHeaders = {
            'Content-Type': 'image/jpeg',
            'Content-Length': tStat.size,
            'Cache-Control': 'public, max-age=86400',
            'X-Content-Type-Options': 'nosniff'
          };
          if (origin && isOriginAllowed(origin)) {
            thumbHeaders['Access-Control-Allow-Origin'] = origin;
          }
          res.writeHead(200, thumbHeaders);
          return fs.createReadStream(thumbFile, { highWaterMark: 256 * 1024 }).pipe(res);
        } catch (e) {
          res.writeHead(500, { 'Content-Type': 'text/plain; charset=utf-8' });
          return res.end(e.message);
        }
      };

      if (fs.existsSync(thumbFile)) {
        try {
          const tStat = fs.statSync(thumbFile);
          if (tStat.size > 0 && tStat.mtimeMs >= stat.mtimeMs) {
            return serveThumb();
          }
        } catch (_) {}
      }

      // Kích hoạt tạo thumbnail ngầm trong hàng đợi, không bao giờ giữ treo kết nối HTTP
      generateThumbnail(targetPath, thumbFile).catch(() => {});

      // Phản hồi 404 ngay lập tức để giải phóng socket kết nối trình duyệt
      const notReadyHeaders = {
        'Content-Type': 'text/plain; charset=utf-8',
        'Cache-Control': 'no-cache, no-store'
      };
      if (origin && isOriginAllowed(origin)) {
        notReadyHeaders['Access-Control-Allow-Origin'] = origin;
      }
      res.writeHead(404, notReadyHeaders);
      return res.end('Thumbnail đang được tạo ngầm');
    }

    res.writeHead(415, { 'Content-Type': 'text/plain; charset=utf-8' });
    return res.end('Định dạng không hỗ trợ thumbnail');
  }

  // API: Tải xuống tệp hoặc xem trực tiếp inline (Hỗ trợ PDF, Text/Code preview & Download Attachment)
  if (pathname === '/api/download') {
    const filePath = url.searchParams.get('path') || '';
    const targetPath = safeResolve(filePath, { mustExist: true });
    if (!targetPath) {
      res.writeHead(404, { 'Content-Type': 'text/plain; charset=utf-8' });
      return res.end('Không tìm thấy tệp');
    }
    const stat = fs.statSync(targetPath);
    if (stat.isDirectory()) {
      res.writeHead(400, { 'Content-Type': 'text/plain; charset=utf-8' });
      return res.end('Không thể tải xuống thư mục');
    }

    const filename = path.basename(targetPath);
    const ext = path.extname(filename).toLowerCase();
    const contentType = mimeTypes[ext] || 'application/octet-stream';
    const isInline = url.searchParams.get('inline') === '1';

    applySecurityHeaders(res, origin);
    const disposition = isInline
      ? `inline; filename="${encodeURIComponent(filename)}"; filename*=UTF-8''${encodeURIComponent(filename)}`
      : `attachment; filename="${encodeURIComponent(filename)}"; filename*=UTF-8''${encodeURIComponent(filename)}`;

    const headers = {
      'Content-Type': contentType,
      'Content-Length': stat.size,
      'Content-Disposition': disposition,
      'X-Content-Type-Options': 'nosniff'
    };
    if (origin && isOriginAllowed(origin)) {
      headers['Access-Control-Allow-Origin'] = origin;
    }
    if (!isInline) {
      headers['Content-Security-Policy'] = "default-src 'none'; sandbox";
    }
    res.writeHead(200, headers);
    return fs.createReadStream(targetPath, { highWaterMark: 1024 * 1024 }).pipe(res);
  }

  // =========================================================================
  // Phục vụ Static Files cho Frontend (Bảo vệ Dotfiles & Mã nguồn nhạy cảm)
  // =========================================================================
  let reqFile = pathname === '/' ? 'index.html' : pathname.replace(/^\/+/, '');

  if (reqFile.includes('\0') || reqFile.split('/').includes('..')) {
    res.writeHead(400, { 'Content-Type': 'text/plain; charset=utf-8' });
    return res.end('Bad Request');
  }

  // Chặn dotfiles (.env, .git, .admin_secret, v.v.)
  const reqSegments = reqFile.split('/');
  if (reqSegments.some(seg => seg.startsWith('.'))) {
    res.writeHead(403, { 'Content-Type': 'text/plain; charset=utf-8' });
    return res.end('403 Forbidden');
  }

  // Chặn truy cập các tệp mã nguồn backend & cấu hình nhạy cảm
  const blockedExts = ['.mjs', '.json', '.env', '.sh', '.service', '.conf', '.secret', '.key', '.bak', '.log'];
  const reqExt = path.extname(reqFile).toLowerCase();
  if (blockedExts.includes(reqExt)) {
    res.writeHead(403, { 'Content-Type': 'text/plain; charset=utf-8' });
    return res.end('403 Forbidden: Protected File');
  }

  const staticPath = path.resolve(PUBLIC_DIR, reqFile);
  if (!staticPath.startsWith(PUBLIC_DIR + path.sep) && staticPath !== PUBLIC_DIR && staticPath !== path.join(PUBLIC_DIR, 'index.html')) {
    res.writeHead(403, { 'Content-Type': 'text/plain; charset=utf-8' });
    return res.end('403 Forbidden');
  }

  if (!fs.existsSync(staticPath)) {
    const fallbackHtml = path.join(PUBLIC_DIR, 'index.html');
    if (fs.existsSync(fallbackHtml)) {
      applySecurityHeaders(res, origin);
      res.writeHead(200, { 'Content-Type': 'text/html; charset=utf-8' });
      return fs.createReadStream(fallbackHtml).pipe(res);
    }
    res.writeHead(404, { 'Content-Type': 'text/plain; charset=utf-8' });
    return res.end('404 Not Found');
  }

  const stat = fs.statSync(staticPath);
  if (stat.isDirectory()) {
    const idx = path.join(staticPath, 'index.html');
    if (fs.existsSync(idx)) {
      applySecurityHeaders(res, origin);
      res.writeHead(200, { 'Content-Type': 'text/html; charset=utf-8' });
      return fs.createReadStream(idx).pipe(res);
    }
    res.writeHead(403, { 'Content-Type': 'text/plain; charset=utf-8' });
    return res.end('403 Forbidden');
  }

  const contentType = mimeTypes[reqExt] || 'application/octet-stream';
  applySecurityHeaders(res, origin);
  res.writeHead(200, {
    'Content-Type': contentType,
    'Content-Length': stat.size,
    'Cache-Control': (reqExt === '.html' || reqExt === '.js' || reqExt === '.css') ? 'no-cache, must-revalidate' : 'public, max-age=86400'
  });
  fs.createReadStream(staticPath).pipe(res);
});

server.listen(PORT, HOST, () => {
  console.log(`[NAS Drive] Máy chủ Google Drive NAS đang chạy tại http://${HOST}:${PORT}`);
  console.log(`[NAS Drive] Thư mục lưu trữ 600GB: ${STORAGE_ROOT}`);
  console.log(`[NAS Security] Hệ thống phòng thủ bảo mật & xác thực đã kích hoạt.`);
});
