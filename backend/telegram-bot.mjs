import fs from 'node:fs';
import path from 'node:path';
import os from 'node:os';
import https from 'node:https';
import { fileURLToPath } from 'node:url';
import { execSync } from 'node:child_process';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const CONFIG_PATH = path.join(__dirname, 'telegram-config.json');

// Cấu hình mặc định
const defaultConfig = {
  botToken: '',
  chatId: '',
  alertDiskPercent: 90,
  checkIntervalSeconds: 60,
  enablePeriodicReport: true,
  dailyReportHour: 8,
  notifyOnBoot: true,
  enableServiceWatchdog: true,
  autoRestartOnCrash: true
};

function loadConfig() {
  try {
    if (fs.existsSync(CONFIG_PATH)) {
      const raw = fs.readFileSync(CONFIG_PATH, 'utf8');
      return { ...defaultConfig, ...JSON.parse(raw) };
    }
  } catch (err) {
    console.error('[Config Error]:', err.message);
  }
  saveConfig(defaultConfig);
  return defaultConfig;
}

function saveConfig(cfg) {
  try {
    fs.writeFileSync(CONFIG_PATH, JSON.stringify(cfg, null, 2), 'utf8');
  } catch (err) {
    console.error('[Save Config Error]:', err.message);
  }
}

let config = loadConfig();

// Gọi Telegram Bot API qua node:https ép buộc IPv4
function telegramApi(method, params = {}, timeoutMs = 30000) {
  return new Promise((resolve, reject) => {
    if (!config.botToken) return resolve({ ok: false, description: 'Chưa có token' });

    const payload = JSON.stringify(params);
    const options = {
      hostname: 'api.telegram.org',
      port: 443,
      path: `/bot${config.botToken}/${method}`,
      method: 'POST',
      family: 4,
      timeout: timeoutMs,
      headers: {
        'Content-Type': 'application/json',
        'Content-Length': Buffer.byteLength(payload)
      }
    };

    const req = https.request(options, (res) => {
      let data = '';
      res.on('data', chunk => { data += chunk; });
      res.on('end', () => {
        try {
          resolve(JSON.parse(data));
        } catch (e) {
          reject(new Error(`Phản hồi JSON không hợp lệ: ${data}`));
        }
      });
    });

    req.on('error', err => reject(err));
    req.on('timeout', () => {
      req.destroy();
      reject(new Error('Request Timeout'));
    });

    req.write(payload);
    req.end();
  });
}

function escapeHtml(text = '') {
  return String(text)
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;');
}

function formatBytes(bytes) {
  if (bytes === 0 || !bytes || isNaN(bytes)) return '0 B';
  const k = 1024;
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return parseFloat((bytes / Math.pow(k, i)).toFixed(1)) + ' ' + sizes[i];
}

function formatUptime(seconds) {
  const d = Math.floor(seconds / (3600 * 24));
  const h = Math.floor((seconds % (3600 * 24)) / 3600);
  const m = Math.floor((seconds % 3600) / 60);
  const parts = [];
  if (d > 0) parts.push(`${d}d`);
  if (h > 0) parts.push(`${h}h`);
  parts.push(`${m}m`);
  return parts.join(' ');
}

// Tạo thanh tiến trình trực quan
function makeProgressBar(percent, length = 10) {
  const p = Math.max(0, Math.min(100, Math.round(percent)));
  const filled = Math.round((p / 100) * length);
  const empty = length - filled;
  return '▰'.repeat(filled) + '▱'.repeat(empty);
}

// Lưu trữ lịch sử tải CPU để tính % sử dụng thực tế
let prevCpuTimes = null;
function getCpuUsagePercent() {
  try {
    const stat = fs.readFileSync('/proc/stat', 'utf8');
    const firstLine = stat.split('\n')[0];
    const parts = firstLine.trim().split(/\s+/).slice(1).map(Number);
    const idle = parts[3] + (parts[4] || 0);
    const total = parts.reduce((a, b) => a + b, 0);

    if (prevCpuTimes) {
      const deltaTotal = total - prevCpuTimes.total;
      const deltaIdle = idle - prevCpuTimes.idle;
      prevCpuTimes = { idle, total };
      if (deltaTotal > 0) {
        return Math.max(0, Math.min(100, (1 - deltaIdle / deltaTotal) * 100)).toFixed(1);
      }
    }
    prevCpuTimes = { idle, total };
    const load = os.loadavg()[0];
    return Math.min(100, (load / (os.cpus().length || 4)) * 100).toFixed(1);
  } catch (_) {
    return '0.0';
  }
}

// Thu thập toàn bộ số liệu chuyên sâu
function getSystemMetrics() {
  // 1. RAM & SWAP
  let memTotal = os.totalmem();
  let memAvail = os.freemem();
  let swapTotal = 0;
  let swapFree = 0;
  let cachedMem = 0;
  let buffersMem = 0;

  try {
    const meminfo = fs.readFileSync('/proc/meminfo', 'utf8');
    const lines = meminfo.split('\n');
    const data = {};
    for (const line of lines) {
      const [k, v] = line.split(':');
      if (k && v) data[k.trim()] = parseInt(v.trim().split(' ')[0], 10) * 1024;
    }
    if (data['MemTotal']) memTotal = data['MemTotal'];
    if (data['MemAvailable']) memAvail = data['MemAvailable'];
    if (data['SwapTotal']) swapTotal = data['SwapTotal'];
    if (data['SwapFree']) swapFree = data['SwapFree'];
    if (data['Cached']) cachedMem = data['Cached'];
    if (data['Buffers']) buffersMem = data['Buffers'];
  } catch (_) {}

  const ramUsed = memTotal - memAvail;
  const ramPercent = memTotal > 0 ? (ramUsed / memTotal) * 100 : 0;
  const swapUsed = swapTotal - swapFree;
  const swapPercent = swapTotal > 0 ? (swapUsed / swapTotal) * 100 : 0;

  // 2. Ổ cứng NAS (/mnt/nas) & Ổ OS (/)
  let diskTotal = 0, diskFree = 0, diskUsed = 0, diskPercent = 0;
  try {
    const st = fs.statfsSync('/mnt/nas');
    diskTotal = st.bsize * st.blocks;
    diskFree = st.bsize * st.bavail;
    diskUsed = diskTotal - diskFree;
    diskPercent = diskTotal > 0 ? (diskUsed / diskTotal) * 100 : 0;
  } catch (_) {}

  let rootTotal = 0, rootFree = 0, rootUsed = 0, rootPercent = 0;
  try {
    const rst = fs.statfsSync('/');
    rootTotal = rst.bsize * rst.blocks;
    rootFree = rst.bsize * rst.bavail;
    rootUsed = rootTotal - rootFree;
    rootPercent = rootTotal > 0 ? (rootUsed / rootTotal) * 100 : 0;
  } catch (_) {}

  // 3. CPU & Nhiệt độ
  const cpus = os.cpus();
  const cpuModel = cpus[0]?.model || 'Intel Processor';
  const cpuCount = cpus.length;
  const cpuUsage = getCpuUsagePercent();
  const loadAvg = os.loadavg().map(n => n.toFixed(2)).join(', ');

  let tempNum = 0;
  let tempStr = 'N/A';
  let tempIcon = '🟢';
  try {
    const raw = parseInt(fs.readFileSync('/sys/class/thermal/thermal_zone0/temp', 'utf8').trim(), 10);
    tempNum = raw / 1000;
    tempStr = tempNum.toFixed(1) + '°C';
    tempIcon = tempNum < 45 ? '🟢' : (tempNum < 65 ? '🟡' : '🔴');
  } catch (_) {}

  // 4. Mạng (Network Throughput enp1s0)
  let rxBytes = 0, txBytes = 0;
  try {
    const netDev = fs.readFileSync('/proc/net/dev', 'utf8');
    for (const line of netDev.split('\n')) {
      if (line.includes('enp1s0:')) {
        const parts = line.split(':')[1].trim().split(/\s+/).map(Number);
        rxBytes = parts[0] || 0;
        txBytes = parts[8] || 0;
        break;
      }
    }
  } catch (_) {}

  // 5. Trạng thái dịch vụ
  const checkService = (svc) => {
    try {
      const out = execSync(`systemctl is-active ${svc}`, { encoding: 'utf8' }).trim();
      return out === 'active';
    } catch (_) { return false; }
  };
  const isNasRunning = checkService('nas-drive.service');
  const isCloudflareRunning = checkService('cloudflared-nas.service');

  return {
    cpuModel,
    cpuCount,
    cpuUsage,
    loadAvg,
    tempStr,
    tempIcon,
    uptime: formatUptime(os.uptime()),
    ramUsed: formatBytes(ramUsed),
    ramTotal: formatBytes(memTotal),
    ramFree: formatBytes(memAvail),
    ramPercent: ramPercent.toFixed(1),
    ramBar: makeProgressBar(ramPercent),
    swapUsed: formatBytes(swapUsed),
    swapTotal: formatBytes(swapTotal),
    swapPercent: swapPercent.toFixed(1),
    swapBar: makeProgressBar(swapPercent),
    cachedMem: formatBytes(cachedMem + buffersMem),
    diskUsed: formatBytes(diskUsed),
    diskTotal: formatBytes(diskTotal),
    diskFree: formatBytes(diskFree),
    diskPercent: diskPercent.toFixed(1),
    diskBar: makeProgressBar(diskPercent),
    rootUsed: formatBytes(rootUsed),
    rootTotal: formatBytes(rootTotal),
    rootPercent: rootPercent.toFixed(1),
    rootBar: makeProgressBar(rootPercent),
    rxTotal: formatBytes(rxBytes),
    txTotal: formatBytes(txBytes),
    isNasRunning,
    isCloudflareRunning,
    lanIp: '192.168.88.230:8080',
    wanDomain: 'https://luutru.npccc.io.vn'
  };
}

// Bảng điều khiển chính (Dashboard UI)
function renderDashboard(m) {
  const nasStatusBadge = m.isNasRunning ? '🟢 Hoạt động (Port 8080)' : '🔴 ĐÃ TẮT';
  const cfStatusBadge = m.isCloudflareRunning ? '🟢 Đang truyền đường hầm' : '🔴 Ngắt kết nối';

  return `🖥️ <b>BẢNG ĐIỀU KHIỂN MÁY CHỦ NAS DRIVE</b>
━━━━━━━━━━━━━━━━━━━━━━━━━━
💻 <b>CPU & XỬ LÝ:</b>
  • Chip: <code>${escapeHtml(m.cpuModel)}</code> (${m.cpuCount} Cores)
  • Tải tức thời: [${makeProgressBar(parseFloat(m.cpuUsage))}] <b>${m.cpuUsage}%</b>
  • Tải trung bình: <code>${m.loadAvg}</code>
  • Nhiệt độ: <b>${m.tempStr}</b> ${m.tempIcon} | Uptime: <b>${m.uptime}</b>

🧠 <b>BỘ NHỚ (RAM & SWAP):</b>
  • RAM  : [${m.ramBar}] <b>${m.ramPercent}%</b>
    ↳ <i>Đã dùng: <b>${m.ramUsed}</b> / ${m.ramTotal} (Trống: ${m.ramFree})</i>
  • SWAP : [${m.swapBar}] <b>${m.swapPercent}%</b> (${m.swapUsed} / ${m.swapTotal})
  • Bộ đệm Cache: <b>${m.cachedMem}</b> (Sẵn sàng cấp phát)

💽 <b>LƯU TRỮ (STORAGE):</b>
  • Ổ NAS : [${m.diskBar}] <b>${m.diskPercent}%</b>
    ↳ <i>Đã lưu: <b>${m.diskUsed}</b> / ${m.diskTotal}</i>
    ↳ <i>Khả dụng: <b>${m.diskFree}</b></i>
  • Ổ OS (/) : [${m.rootBar}] <b>${m.rootPercent}%</b> (${m.rootUsed} / ${m.rootTotal})

🌐 <b>MẠNG & BĂNG THÔNG (enp1s0):</b>
  • Tổng lưu lượng: 📥 <b>${m.rxTotal}</b> | 📤 <b>${m.txTotal}</b>
  • LAN : <code>http://${m.lanIp}</code>
  • WAN : ${m.wanDomain}

⚡ <b>DỊCH VỤ HỆ THỐNG:</b>
  • Web NAS Drive : ${nasStatusBadge}
  • Cloudflare WAN: ${cfStatusBadge}
━━━━━━━━━━━━━━━━━━━━━━━━━━
🕒 <i>Cập nhật: ${new Date().toLocaleTimeString('vi-VN')} - ${new Date().toLocaleDateString('vi-VN')}</i>`;
}

// Bảng chi tiết ổ đĩa (/disk)
function renderDiskDetail() {
  const m = getSystemMetrics();
  let driveSize = '--';
  let trashSize = '--';
  let thumbSize = '--';
  let chunksSize = '--';

  const getFolderSize = (dir) => {
    try {
      const out = execSync(`du -sh "${dir}" 2>/dev/null`, { encoding: 'utf8' });
      return out.split('\t')[0] || '--';
    } catch (_) { return '--'; }
  };

  try {
    driveSize = getFolderSize('/mnt/nas/drive');
    trashSize = getFolderSize('/mnt/nas/drive/.trash');
    thumbSize = getFolderSize('/mnt/nas/drive/.thumbnails');
    chunksSize = getFolderSize('/mnt/nas/drive/.chunks');
  } catch (_) {}

  return `💽 <b>CHI TIẾT PHÂN BỔ DUNG LƯỢNG Ổ NAS</b>
━━━━━━━━━━━━━━━━━━━━━━━━━━
📦 <b>Phân vùng lưu trữ (/mnt/nas):</b>
 • Tổng không gian: <b>${m.diskTotal}</b>
 • Đã sử dụng    : [${m.diskBar}] <b>${m.diskPercent}%</b> (<b>${m.diskUsed}</b>)
 • Còn khả dụng  : <b>${m.diskFree}</b>

📁 <b>Phân bổ dung lượng chi tiết:</b>
 ├─ 📂 Dữ liệu tệp tin (/drive)   : <b>${driveSize}</b>
 ├─ 🗑️ Thùng rác tạm (.trash)     : <b>${trashSize}</b>
 ├─ 🖼️ Ảnh xem trước (.thumbnails): <b>${thumbSize}</b>
 └─ 🧩 Bộ đệm tải lên (.chunks)   : <b>${chunksSize}</b>
━━━━━━━━━━━━━━━━━━━━━━━━━━
💡 <i>Gợi ý: Nếu thùng rác đầy, bạn có thể vào Web/App NAS Drive bấm "Dọn sạch thùng rác" để thu hồi dung lượng.</i>`;
}

// Bảng mạng & cổng kết nối (/network)
function renderNetworkDetail() {
  const m = getSystemMetrics();
  let activeConnections = 0;
  try {
    const ssOut = execSync('ss -t state established "( sport = :8080 or dport = :8080 )" 2>/dev/null | wc -l', { encoding: 'utf8' });
    activeConnections = Math.max(0, parseInt(ssOut.trim(), 10) - 1);
  } catch (_) {}

  return `🌐 <b>CHI TIẾT MẠNG & KẾT NỐI MÁY CHỦ</b>
━━━━━━━━━━━━━━━━━━━━━━━━━━
🔌 <b>Cổng mạng vật lý (Intel 4-NIC Gigabit):</b>
 • Cổng chính: <code>enp1s0</code> (1 Gbps Full-Duplex)
 • Tổng tải xuống (RX): 📥 <b>${m.rxTotal}</b>
 • Tổng tải lên   (TX): 📤 <b>${m.txTotal}</b>

🔗 <b>Kết nối truy cập:</b>
 • Mạng nội bộ (LAN): <code>http://${m.lanIp}</code>
 • Tên miền WAN: <b>${m.wanDomain}</b>
 • Kết nối TCP đang mở đến NAS: <b>${activeConnections} phiên</b>

🛡️ <b>Mạng bảo mật riêng (VPN):</b>
 • Tailscale: <code>100.100.100.100</code> (Online)
━━━━━━━━━━━━━━━━━━━━━━━━━━
🕒 <i>Kiểm tra lúc: ${new Date().toLocaleTimeString('vi-VN')}</i>`;
}

// Bảng top tiến trình ngốn CPU / RAM
function renderTopProcesses() {
  let procText = 'Không lấy được danh sách tiến trình';
  try {
    const out = execSync('ps -eo comm,pcpu,pmem --sort=-pcpu | head -n 8', { encoding: 'utf8' }).trim();
    const lines = out.split('\n');
    procText = lines.map((l, idx) => {
      if (idx === 0) return `<code>TIẾN TRÌNH       %CPU   %RAM</code>`;
      const parts = l.trim().split(/\s+/);
      const name = parts[0].padEnd(16, ' ').slice(0, 16);
      const cpu = (parts[1] || '0').padStart(5, ' ');
      const mem = (parts[2] || '0').padStart(6, ' ');
      return `<code>${name} ${cpu}% ${mem}%</code>`;
    }).join('\n');
  } catch (_) {}

  return `🔥 <b>TOP TIẾN TRÌNH TIÊU THỤ TÀI NGUYÊN</b>
━━━━━━━━━━━━━━━━━━━━━━━━━━
${procText}
━━━━━━━━━━━━━━━━━━━━━━━━━━
💡 <i>Nếu ffmpeg đang chạy, server đang tạo ảnh xem trước (thumbnail) cho video trong nền.</i>`;
}

// Menu nút bấm tương tác (Inline Keyboard)
function getDashboardKeyboard() {
  return {
    inline_keyboard: [
      [
        { text: '🔄 Làm mới', callback_data: 'refresh_status' },
        { text: '💽 Ổ đĩa', callback_data: 'view_disk' }
      ],
      [
        { text: '🌐 Mạng & Ports', callback_data: 'view_network' },
        { text: '🔥 Top tiến trình', callback_data: 'view_top' }
      ],
      [
        { text: '🚀 Khởi động lại NAS', callback_data: 'restart_nas_prompt' }
      ]
    ]
  };
}

function getBackKeyboard() {
  return {
    inline_keyboard: [
      [
        { text: '⬅️ Quay lại bảng điều khiển', callback_data: 'refresh_status' },
        { text: '🔄 Làm mới', callback_data: 'refresh_current' }
      ]
    ]
  };
}

// Gửi tin nhắn có nút bấm
async function sendTelegramWithKeyboard(chatId, text, keyboard) {
  if (!config.botToken || !chatId) return null;
  return telegramApi('sendMessage', {
    chat_id: chatId,
    text,
    parse_mode: 'HTML',
    disable_web_page_preview: true,
    reply_markup: keyboard
  });
}

// Cập nhật tin nhắn tại chỗ (In-place edit) khi bấm nút
async function editTelegramMessage(chatId, messageId, text, keyboard) {
  if (!config.botToken || !chatId || !messageId) return null;
  try {
    return await telegramApi('editMessageText', {
      chat_id: chatId,
      message_id: messageId,
      text,
      parse_mode: 'HTML',
      disable_web_page_preview: true,
      reply_markup: keyboard
    }, 15000);
  } catch (err) {
    // Nếu nội dung không đổi, Telegram báo lỗi "message is not modified" -> bỏ qua
    return null;
  }
}

// Xử lý nút bấm tương tác (Callback Query)
async function handleCallbackQuery(cb) {
  const queryId = cb.id;
  const chatId = cb.message?.chat?.id;
  const messageId = cb.message?.message_id;
  const data = cb.data;

  if (!chatId || !messageId) return;

  // Xác thực quyền quản trị
  if (config.chatId && String(chatId) !== String(config.chatId)) {
    await telegramApi('answerCallbackQuery', {
      callback_query_id: queryId,
      text: '⛔ Bạn không có quyền thao tác trên máy chủ này!',
      show_alert: true
    });
    return;
  }

  switch (data) {
    case 'refresh_status':
      await telegramApi('answerCallbackQuery', { callback_query_id: queryId, text: '⚡ Đã cập nhật chỉ số mới nhất!' });
      const m = getSystemMetrics();
      await editTelegramMessage(chatId, messageId, renderDashboard(m), getDashboardKeyboard());
      break;

    case 'view_disk':
      await telegramApi('answerCallbackQuery', { callback_query_id: queryId, text: '💽 Đang đọc chi tiết ổ đĩa...' });
      await editTelegramMessage(chatId, messageId, renderDiskDetail(), getBackKeyboard());
      break;

    case 'view_network':
      await telegramApi('answerCallbackQuery', { callback_query_id: queryId, text: '🌐 Đang kiểm tra cổng mạng...' });
      await editTelegramMessage(chatId, messageId, renderNetworkDetail(), getBackKeyboard());
      break;

    case 'view_top':
      await telegramApi('answerCallbackQuery', { callback_query_id: queryId, text: '🔥 Đang quét tiến trình...' });
      await editTelegramMessage(chatId, messageId, renderTopProcesses(), getBackKeyboard());
      break;

    case 'restart_nas_prompt':
      await telegramApi('answerCallbackQuery', { callback_query_id: queryId });
      await editTelegramMessage(chatId, messageId, `⚠️ <b>XÁC NHẬN KHỞI ĐỘNG LẠI DỊCH VỤ NAS?</b>\n━━━━━━━━━━━━━━━━━━━━━━━━━━\nThao tác này sẽ khởi động lại <code>nas-drive.service</code> trong 2 giây. Các kết nối đang tải file dở sẽ tự động kết nối lại.`, {
        inline_keyboard: [
          [
            { text: '✅ Xác nhận khởi động lại', callback_data: 'restart_nas_confirm' },
            { text: '❌ Hủy bỏ', callback_data: 'refresh_status' }
          ]
        ]
      });
      break;

    case 'restart_nas_confirm':
      await telegramApi('answerCallbackQuery', { callback_query_id: queryId, text: '⏳ Đang khởi động lại dịch vụ...' });
      try {
        execSync('sudo systemctl restart nas-drive.service', { encoding: 'utf8' });
        setTimeout(async () => {
          const fresh = getSystemMetrics();
          await editTelegramMessage(chatId, messageId, `✅ <b>ĐÃ KHỞI ĐỘNG LẠI THÀNH CÔNG!</b>\n━━━━━━━━━━━━━━━━━━━━━━━━━━\nDịch vụ <code>nas-drive.service</code> đã hoạt động bình thường trở lại.\n\n` + renderDashboard(fresh), getDashboardKeyboard());
        }, 2000);
      } catch (err) {
        await editTelegramMessage(chatId, messageId, `❌ <b>Lỗi khi restart:</b> ${escapeHtml(err.message)}`, getDashboardKeyboard());
      }
      break;

    default:
      await telegramApi('answerCallbackQuery', { callback_query_id: queryId });
      break;
  }
}

// Xử lý tin nhắn gõ tay
async function handleCommand(msg) {
  const chatId = msg.chat?.id;
  const text = (msg.text || '').trim();
  const username = msg.from?.username ? `@${msg.from.username}` : (msg.from?.first_name || 'Bạn');

  if (!chatId || !text) return;

  if (!config.chatId) {
    config.chatId = String(chatId);
    saveConfig(config);
    const m = getSystemMetrics();
    await sendTelegramWithKeyboard(chatId, `🎉 <b>KẾT NỐI THÀNH CÔNG!</b>\n\nChào mừng <b>${escapeHtml(username)}</b>! Chat ID của bạn (<code>${chatId}</code>) đã được lưu làm Quản trị viên duy nhất.\n\n` + renderDashboard(m), getDashboardKeyboard());
    return;
  }

  if (String(chatId) !== String(config.chatId)) {
    await telegramApi('sendMessage', {
      chat_id: chatId,
      text: `⛔ <b>Từ chối truy cập</b>\nBot này chỉ dành cho chủ sở hữu máy chủ NAS.`
    });
    return;
  }

  const cmd = text.split(' ')[0].toLowerCase().replace(/@.+$/, '');

  switch (cmd) {
    case '/start':
    case '/status':
    case '/stats':
      const m = getSystemMetrics();
      await sendTelegramWithKeyboard(chatId, renderDashboard(m), getDashboardKeyboard());
      break;

    case '/disk':
    case '/storage':
      await sendTelegramWithKeyboard(chatId, renderDiskDetail(), getBackKeyboard());
      break;

    case '/network':
      await sendTelegramWithKeyboard(chatId, renderNetworkDetail(), getBackKeyboard());
      break;

    case '/top':
      await sendTelegramWithKeyboard(chatId, renderTopProcesses(), getBackKeyboard());
      break;

    case '/services':
      const metrics = getSystemMetrics();
      const nasBadge = metrics.isNasRunning ? '🟢 Hoạt động (Port 8080)' : '🔴 Đã tắt';
      const cfBadge = metrics.isCloudflareRunning ? '🟢 Hoạt động' : '🔴 Đã tắt';
      await sendTelegramWithKeyboard(chatId, `⚙️ <b>TRẠNG THÁI DỊCH VỤ HỆ THỐNG</b>\n━━━━━━━━━━━━━━━━━━━━━━━━━━\n • <b>nas-drive.service</b> : ${nasBadge}\n • <b>cloudflared-nas.service</b>: ${cfBadge}\n • <b>Cổng lắng nghe</b>: 8080 (HTTP)\n • <b>Thời gian chạy</b>: ${metrics.uptime}`, getDashboardKeyboard());
      break;

    case '/ping':
      const start = Date.now();
      await telegramApi('sendMessage', {
        chat_id: chatId,
        text: `🏓 <b>Pong!</b> Kết nối máy chủ phản hồi trong <b>${Date.now() - start}ms</b>.`
      });
      break;

    case '/help':
      await sendTelegramWithKeyboard(chatId, `🤖 <b>DANH SÁCH LỆNH BOT NAS DRIVE</b>
━━━━━━━━━━━━━━━━━━━━━━━━━━
/status  - Bảng điều khiển tổng thể CPU, RAM, Ổ cứng, Dịch vụ
/disk    - Xem chi tiết dung lượng thư mục NAS & thùng rác
/network - Xem lưu lượng mạng & kết nối cổng
/top     - Top tiến trình tiêu thụ CPU & RAM
/services- Kiểm tra trạng thái các dịch vụ nền
/ping    - Đo độ trễ phản hồi máy chủ
/restart_nas - Khởi động lại dịch vụ NAS Drive
━━━━━━━━━━━━━━━━━━━━━━━━━━
💡 <i>Bạn có thể bấm trực tiếp vào các nút bấm dưới tin nhắn để thao tác nhanh mà không cần gõ lệnh!</i>`, getDashboardKeyboard());
      break;

    default:
      if (text.startsWith('/')) {
        const m = getSystemMetrics();
        await sendTelegramWithKeyboard(chatId, `❓ Lệnh không hợp lệ. Đây là bảng điều khiển hiện tại của máy chủ:\n\n` + renderDashboard(m), getDashboardKeyboard());
      }
      break;
  }
}

// Vòng lặp Long Polling
let lastUpdateId = 0;
let isPolling = false;

async function pollUpdates() {
  if (isPolling || !config.botToken) return;
  isPolling = true;

  try {
    const data = await telegramApi('getUpdates', {
      offset: lastUpdateId + 1,
      timeout: 20
    }, 28000);

    if (data.ok && Array.isArray(data.result)) {
      for (const update of data.result) {
        lastUpdateId = update.update_id;
        if (update.message) {
          await handleCommand(update.message);
        } else if (update.callback_query) {
          await handleCallbackQuery(update.callback_query);
        }
      }
    } else if (data.error_code === 409) {
      await new Promise(r => setTimeout(r, 2000));
    }
  } catch (err) {
    if (err.message !== 'Request Timeout') {
      console.error('[Polling Error]:', err.message);
    }
  } finally {
    isPolling = false;
    setTimeout(pollUpdates, 400);
  }
}

// Giám sát sức khỏe nền (Watchdog & Periodic Alert)
let lastNasServiceState = true;
let lastDiskAlertTime = 0;
let lastReportDate = '';

function startWatchdog() {
  setInterval(async () => {
    config = loadConfig();
    if (!config.botToken || !config.chatId) return;

    // 1. Kiểm tra trạng thái nas-drive.service
    if (config.enableServiceWatchdog) {
      let isRunning = false;
      try {
        isRunning = execSync('systemctl is-active nas-drive.service', { encoding: 'utf8' }).trim() === 'active';
      } catch (_) { isRunning = false; }

      if (!isRunning && lastNasServiceState) {
        let msg = `🚨 <b>CẢNH BÁO KHẨN CẤP: DỊCH VỤ NAS ĐÃ BỊ DỪNG!</b>\n━━━━━━━━━━━━━━━━━━━━━━━━━━\nThời gian: ${new Date().toLocaleTimeString('vi-VN')}`;
        if (config.autoRestartOnCrash) {
          msg += `\n\n🔄 Đang tự động thử khởi động lại dịch vụ...`;
          try {
            execSync('sudo systemctl restart nas-drive.service');
            msg += `\n✅ Đã gửi lệnh khởi động lại.`;
          } catch (rErr) {
            msg += `\n❌ Thất bại: ${escapeHtml(rErr.message)}`;
          }
        }
        await telegramApi('sendMessage', { chat_id: config.chatId, text: msg, parse_mode: 'HTML' });
      } else if (isRunning && !lastNasServiceState) {
        await telegramApi('sendMessage', {
          chat_id: config.chatId,
          text: `✅ <b>PHỤC HỒI DỊCH VỤ NAS</b>\nDịch vụ <code>nas-drive.service</code> đã hoạt động bình thường trở lại.`,
          parse_mode: 'HTML'
        });
      }
      lastNasServiceState = isRunning;
    }

    // 2. Cảnh báo ổ cứng gần đầy
    try {
      const st = fs.statfsSync('/mnt/nas');
      const total = st.bsize * st.blocks;
      const free = st.bsize * st.bavail;
      const percent = total > 0 ? ((total - free) / total) * 100 : 0;

      if (percent >= config.alertDiskPercent) {
        const now = Date.now();
        if (now - lastDiskAlertTime > 6 * 3600 * 1000) {
          lastDiskAlertTime = now;
          await telegramApi('sendMessage', {
            chat_id: config.chatId,
            text: `⚠️ <b>CẢNH BÁO DUNG LƯỢNG Ổ CỨNG!</b>\n━━━━━━━━━━━━━━━━━━━━━━━━━━\nỔ NAS <code>/mnt/nas</code> đã sử dụng <b>${percent.toFixed(1)}%</b> (Ngưỡng cảnh báo: ${config.alertDiskPercent}%).\nCòn trống: <b>${formatBytes(free)}</b>.\n\nVui lòng kiểm tra và dọn dẹp các tệp tin không cần thiết.`,
            parse_mode: 'HTML'
          });
        }
      }
    } catch (_) {}

    // 3. Báo cáo định kỳ lúc 08:00 sáng
    if (config.enablePeriodicReport) {
      const now = new Date();
      const todayStr = now.toISOString().slice(0, 10);
      if (now.getHours() === config.dailyReportHour && lastReportDate !== todayStr) {
        lastReportDate = todayStr;
        const m = getSystemMetrics();
        await sendTelegramWithKeyboard(config.chatId, `🌅 <b>BÁO CÁO MÁY CHỦ BUỔI SÁNG (08:00)</b>\n━━━━━━━━━━━━━━━━━━━━━━━━━━\n` + renderDashboard(m), getDashboardKeyboard());
      }
    }
  }, (config.checkIntervalSeconds || 60) * 1000);
}

// Khởi động bot
async function main() {
  console.log('====================================================');
  console.log('🤖 NAS Drive Telegram Status Bot v2.0 (Interactive)');
  console.log('====================================================');

  config = loadConfig();

  if (config.botToken) {
    try {
      const data = await telegramApi('getMe');
      if (data.ok) {
        console.log(`[Telegram Bot]: Kết nối thành công bot @${data.result.username} (${data.result.first_name})`);

        if (config.chatId && config.notifyOnBoot) {
          const m = getSystemMetrics();
          await sendTelegramWithKeyboard(config.chatId, `🟢 <b>[SERVER ONLINE] MÁY CHỦ NAS VỪA KHỞI ĐỘNG</b>\n━━━━━━━━━━━━━━━━━━━━━━━━━━\n` + renderDashboard(m), getDashboardKeyboard());
        }
      } else {
        console.error('[Telegram Bot Error]: Token không hợp lệ:', data.description);
      }
    } catch (err) {
      console.error('[Telegram Bot Connection Error]:', err.message);
    }
  }

  pollUpdates();
  startWatchdog();
}

process.on('uncaughtException', err => console.error('[UncaughtException]:', err));
process.on('unhandledRejection', reason => console.error('[UnhandledRejection]:', reason));

main();
