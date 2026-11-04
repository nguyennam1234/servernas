// Google Drive NAS - Client Application (High Performance, Chunked Upload & Clean UI)

// =========================================================================
// UTILITY FUNCTIONS
// =========================================================================
function formatBytes(bytes) {
  if (!bytes || bytes <= 0) return '0 B';
  const k = 1024;
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return parseFloat((bytes / Math.pow(k, i)).toFixed(1)) + ' ' + sizes[i];
}

const API_BASE = (typeof window !== 'undefined' && window.API_BASE_URL)
  ? window.API_BASE_URL.replace(/\/+$/, '')
  : '';

function getAuthToken() {
  try {
    return localStorage.getItem('nas_auth_token') || '';
  } catch (_) {
    return '';
  }
}

function setAuthToken(token) {
  try {
    if (token) {
      localStorage.setItem('nas_auth_token', token);
    } else {
      localStorage.removeItem('nas_auth_token');
    }
  } catch (_) {}
}

function api(endpoint) {
  const clean = endpoint.startsWith('/') ? endpoint : `/${endpoint}`;
  let url = `${API_BASE}${clean}`;
  const token = getAuthToken();
  if (token) {
    const delim = url.includes('?') ? '&' : '?';
    url = `${url}${delim}token=${encodeURIComponent(token)}`;
  }
  return url;
}

async function authFetch(url, options = {}) {
  const token = getAuthToken();
  const headers = new Headers(options.headers || {});
  if (token && !headers.has('Authorization')) {
    headers.set('Authorization', `Bearer ${token}`);
  }
  const fetchOptions = {
    ...options,
    headers,
    credentials: 'include'
  };

  const response = await fetch(url, fetchOptions);
  if (response.status === 401) {
    showLoginModal();
    const err = new Error('UNAUTHORIZED');
    err.status = 401;
    throw err;
  }
  return response;
}

const state = {
  currentFolder: '',
  currentFilter: 'all',
  isTrash: false,
  isVault: false,
  isGridView: true,
  searchQuery: '',
  items: []
};

const selectedPaths = new Set();
let isBusy = false;

// DOM References
const fileViewContainer = document.querySelector('#fileViewContainer');
const foldersSection = document.querySelector('#foldersSection');
const foldersGrid = document.querySelector('#foldersGrid');
const filesSection = document.querySelector('#filesSection');
const filesGrid = document.querySelector('#filesGrid');
const emptyState = document.querySelector('#emptyState');
const breadcrumb = document.querySelector('#breadcrumb');
const searchInput = document.querySelector('#searchInput');
const searchClearBtn = document.querySelector('#searchClearBtn');
const viewToggleBtn = document.querySelector('#viewToggleBtn');
const viewGridIcon = document.querySelector('#viewGridIcon');
const viewListIcon = document.querySelector('#viewListIcon');
const refreshBtn = document.querySelector('#refreshBtn');
const mobileMenuBtn = document.querySelector('#mobileMenuBtn');
const driveSidebar = document.querySelector('#driveSidebar');

// Selection Bar Elements
const selectionBar = document.querySelector('#selectionBar');
const selectionCountText = document.querySelector('#selectionCountText');
const clearSelectionBtn = document.querySelector('#clearSelectionBtn');
const bulkDownloadBtn = document.querySelector('#bulkDownloadBtn');
const bulkTrashBtn = document.querySelector('#bulkTrashBtn');
const bulkTrashBtnText = document.querySelector('#bulkTrashBtnText');
const bulkRestoreBtn = document.querySelector('#bulkRestoreBtn');
const emptyTrashBtn = document.querySelector('#emptyTrashBtn');
const bulkMoveBtn = document.querySelector('#bulkMoveBtn');

// Move Modal Elements
const moveModal = document.querySelector('#moveModal');
const moveModalTitle = document.querySelector('#moveModalTitle');
const moveModalSubTitle = document.querySelector('#moveModalSubTitle');
const movePickerBreadcrumb = document.querySelector('#movePickerBreadcrumb');
const movePickerList = document.querySelector('#movePickerList');
const moveNewFolderBtn = document.querySelector('#moveNewFolderBtn');
const cancelMoveBtn = document.querySelector('#cancelMoveBtn');
const confirmMoveBtn = document.querySelector('#confirmMoveBtn');

// Vault Elements
const navVaultLink = document.querySelector('#navVaultLink');
const vaultChipStatus = document.querySelector('#vaultChipStatus');
const toolbarLockVaultBtn = document.querySelector('#toolbarLockVaultBtn');
const toolbarVaultChangePwdBtn = document.querySelector('#toolbarVaultChangePwdBtn');
const bulkVaultBtn = document.querySelector('#bulkVaultBtn');
const bulkExportVaultBtn = document.querySelector('#bulkExportVaultBtn');

// Vault Modals
const vaultAuthModal = document.querySelector('#vaultAuthModal');
const vaultModalTitle = document.querySelector('#vaultModalTitle');
const vaultModalDesc = document.querySelector('#vaultModalDesc');
const vaultModalErrorBanner = document.querySelector('#vaultModalErrorBanner');
const vaultPasswordLabel = document.querySelector('#vaultPasswordLabel');
const vaultPasswordInput = document.querySelector('#vaultPasswordInput');
const vaultConfirmGroup = document.querySelector('#vaultConfirmGroup');
const vaultConfirmPasswordInput = document.querySelector('#vaultConfirmPasswordInput');
const cancelVaultAuthBtn = document.querySelector('#cancelVaultAuthBtn');
const confirmVaultAuthBtn = document.querySelector('#confirmVaultAuthBtn');

const vaultChangePwdModal = document.querySelector('#vaultChangePwdModal');
const vaultChangePwdErrorBanner = document.querySelector('#vaultChangePwdErrorBanner');
const vaultOldPwdInput = document.querySelector('#vaultOldPwdInput');
const vaultNewPwdInput = document.querySelector('#vaultNewPwdInput');
const vaultConfirmNewPwdInput = document.querySelector('#vaultConfirmNewPwdInput');
const cancelVaultChangePwdBtn = document.querySelector('#cancelVaultChangePwdBtn');
const confirmVaultChangePwdBtn = document.querySelector('#confirmVaultChangePwdBtn');

// Progress & Loading
const stageLoadingBar = document.querySelector('#stageLoadingBar');
let currentLoadController = null;

// Buttons
const newFabBtn = document.querySelector('#newFabBtn');
const newDropdown = document.querySelector('#newDropdown');
const menuNewFolder = document.querySelector('#menuNewFolder');
const menuUploadFile = document.querySelector('#menuUploadFile');
const menuUploadFolder = document.querySelector('#menuUploadFolder');
const toolbarNewFolderBtn = document.querySelector('#toolbarNewFolderBtn');
const toolbarUploadBtn = document.querySelector('#toolbarUploadBtn');
const hiddenFileInput = document.querySelector('#hiddenFileInput');
const hiddenFolderInput = document.querySelector('#hiddenFolderInput');

// Storage
const storageProgressFill = document.querySelector('#storageProgressFill');
const storageDetailText = document.querySelector('#storageDetailText');

// Modals
const videoModal = document.querySelector('#videoModal');
const videoBackdrop = document.querySelector('#videoBackdrop');
const closeVideoBtn = document.querySelector('#closeVideoBtn');
const driveVideoPlayer = document.querySelector('#driveVideoPlayer');
const videoTitle = document.querySelector('#videoTitle');
const videoDownloadBtn = document.querySelector('#videoDownloadBtn');

const imageModal = document.querySelector('#imageModal');
const imageBackdrop = document.querySelector('#imageBackdrop');
const closeImageBtn = document.querySelector('#closeImageBtn');
const driveImagePreview = document.querySelector('#driveImagePreview');
const imageTitle = document.querySelector('#imageTitle');
const imageDownloadBtn = document.querySelector('#imageDownloadBtn');

const newFolderModal = document.querySelector('#newFolderModal');
const newFolderNameInput = document.querySelector('#newFolderNameInput');
const cancelNewFolderBtn = document.querySelector('#cancelNewFolderBtn');
const confirmNewFolderBtn = document.querySelector('#confirmNewFolderBtn');

const renameModal = document.querySelector('#renameModal');
const renameInput = document.querySelector('#renameInput');
const cancelRenameBtn = document.querySelector('#cancelRenameBtn');
const confirmRenameBtn = document.querySelector('#confirmRenameBtn');
let renameTargetItem = null;

// Upload Drawer
const uploadDrawer = document.querySelector('#uploadDrawer');
const drawerTitle = document.querySelector('#drawerTitle');
const drawerItemsList = document.querySelector('#drawerItemsList');
const minimizeDrawerBtn = document.querySelector('#minimizeDrawerBtn');
const closeDrawerBtn = document.querySelector('#closeDrawerBtn');
const dragDropOverlay = document.querySelector('#dragDropOverlay');

// Icons SVG Map (Modern Vibrant Dual-Tone)
const SVG_ICONS = {
  folder: '<svg class="type-icon folder-icon" viewBox="0 0 24 24"><path fill="#f59e0b" d="M10 4H4c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V8c0-1.1-.9-2-2-2h-8l-2-2z"/><path fill="#fde68a" opacity="0.3" d="M2 18V9c0-1.1.9-2 2-2h6l2 2h8c1.1 0 2 .9 2 2v7c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2z"/></svg>',
  video: '<svg class="type-icon video-icon" viewBox="0 0 24 24"><rect x="2" y="4" width="20" height="16" rx="4" fill="#8b5cf6"/><polygon points="10,8 16,12 10,16" fill="#ffffff"/><circle cx="5" cy="7" r="1" fill="#ddd6fe"/><circle cx="19" cy="7" r="1" fill="#ddd6fe"/></svg>',
  image: '<svg class="type-icon image-icon" viewBox="0 0 24 24"><rect x="3" y="3" width="18" height="18" rx="4" fill="#0ea5e9"/><circle cx="8.5" cy="8.5" r="1.5" fill="#ffffff"/><path fill="#ffffff" opacity="0.9" d="m21 15-5-5L5 21h14c1.1 0 2-.9 2-2z"/></svg>',
  audio: '<svg class="type-icon audio-icon" viewBox="0 0 24 24"><rect x="3" y="3" width="18" height="18" rx="4" fill="#a855f7"/><path fill="#ffffff" d="M12 6v7.28c-.4-.17-.84-.28-1.3-.28-1.8 0-3.2 1.4-3.2 3.2s1.4 3.2 3.2 3.2 3.2-1.4 3.2-3.2V9.5h3.5V6H12z"/></svg>',
  document: '<svg class="type-icon doc-icon" viewBox="0 0 24 24"><path fill="#3b82f6" d="M14 2H6c-1.1 0-2 .9-2 2v16c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V8l-6-6z"/><path fill="#93c5fd" opacity="0.8" d="M14 2v6h6L14 2z"/><line x1="8" y1="13" x2="16" y2="13" stroke="#fff" stroke-width="1.5" stroke-linecap="round"/><line x1="8" y1="17" x2="14" y2="17" stroke="#fff" stroke-width="1.5" stroke-linecap="round"/></svg>',
  archive: '<svg class="type-icon archive-icon" viewBox="0 0 24 24"><rect x="3" y="4" width="18" height="16" rx="3" fill="#10b981"/><path fill="#6ee7b7" opacity="0.8" d="M3 4h18v4H3z"/><rect x="10.5" y="7" width="3" height="4" rx="1" fill="#fff"/></svg>',
  other: '<svg class="type-icon other-icon" viewBox="0 0 24 24"><path fill="#64748b" d="M14 2H6c-1.1 0-2 .9-2 2v16c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V8l-6-6z"/><path fill="#94a3b8" opacity="0.6" d="M14 2v6h6L14 2z"/></svg>'
};

// =========================================================================
// DATA FETCHING & STATE
// =========================================================================
async function fetchStorage() {
  try {
    const res = await authFetch(api('/api/storage'));
    const data = await res.json();
    if (data.ok) {
      storageProgressFill.style.width = `${data.usedPercent}%`;
      storageDetailText.textContent = `Đã dùng ${data.usedGb} GB trong tổng số ${data.totalGb} GB (${data.freeGb} GB còn trống)`;
    }
  } catch (err) {
    if (err.status !== 401) {
      console.warn('Lỗi đọc dung lượng:', err);
    }
  }
}

async function loadFiles() {
  if (currentLoadController) {
    currentLoadController.abort();
  }
  currentLoadController = new AbortController();
  const signal = currentLoadController.signal;

  if (stageLoadingBar) stageLoadingBar.hidden = false;
  updateToolbarForMode();

  if (state.isVault) {
    try {
      const statusRes = await authFetch(api('/api/vault/status'), { signal });
      const statusData = await statusRes.json();
      updateVaultBadge(statusData.isUnlocked);

      if (!statusData.isUnlocked) {
        renderVaultLockedView(statusData.isConfigured);
        renderBreadcrumb();
        openVaultUnlockModal(statusData.isConfigured);
        return;
      }

      const vaultUrl = new URL(api('/api/vault/files'), window.location.origin);
      if (state.currentFolder) vaultUrl.searchParams.set('folder', state.currentFolder);
      const res = await authFetch(vaultUrl.toString(), { signal });
      const data = await res.json();
      if (!data.ok) throw new Error(data.error || 'Lỗi đọc tệp Thư mục bí mật');

      state.items = (data.items || []).map(i => ({ ...i, path: i.path || i.id, isVault: true }));
      renderBreadcrumb();
      renderItems();
      syncSelection();
    } catch (err) {
      if (err.name === 'AbortError') return;
      console.error('Lỗi tải tệp Két sắt:', err);
    } finally {
      if (stageLoadingBar) stageLoadingBar.hidden = true;
      document.querySelectorAll('.drive-card.is-opening').forEach(c => c.classList.remove('is-opening'));
    }
    return;
  }

  try {
    const params = new URLSearchParams();
    if (state.currentFolder) params.set('folder', state.currentFolder);
    if (state.currentFilter !== 'all') params.set('filter', state.currentFilter);
    if (state.searchQuery) params.set('q', state.searchQuery);
    if (state.isTrash) params.set('trash', '1');

    const res = await authFetch(api(`/api/files?${params.toString()}`), { signal });
    const data = await res.json();
    if (!data.ok) throw new Error(data.error || 'Lỗi đọc tệp');

    state.items = data.items || [];

    // Lọc lại các mục đã chọn nếu không còn tồn tại
    const currentPaths = new Set(state.items.map(i => i.path));
    for (const p of selectedPaths) {
      if (!currentPaths.has(p)) selectedPaths.delete(p);
    }

    renderBreadcrumb();
    renderItems();
    syncSelection();
  } catch (err) {
    if (err.name === 'AbortError') return;
    console.error('Lỗi tải danh sách tệp:', err);
  } finally {
    if (stageLoadingBar) stageLoadingBar.hidden = true;
    document.querySelectorAll('.drive-card.is-opening').forEach(c => c.classList.remove('is-opening'));
  }
}

// =========================================================================
// NAVIGATION & BROWSER HISTORY (BACK / FORWARD / SWIPE GESTURE)
// =========================================================================
function navigateToFolder(folderPath, pushHistory = true) {
  if (state.currentFolder === folderPath) return;

  if (pushHistory) {
    const url = folderPath ? `?folder=${encodeURIComponent(folderPath)}` : window.location.pathname;
    try {
      history.pushState({
        folder: folderPath,
        filter: state.currentFilter,
        isTrash: state.isTrash,
        isVault: state.isVault
      }, '', url);
    } catch (_) {}
  }

  state.currentFolder = folderPath;
  selectedPaths.clear();
  renderBreadcrumb();
  if (stageLoadingBar) stageLoadingBar.hidden = false;
  loadFiles();
}

function navigateToParentFolder() {
  if (state.currentFolder) {
    const idx = state.currentFolder.lastIndexOf('/');
    const parent = idx !== -1 ? state.currentFolder.substring(0, idx) : '';
    navigateToFolder(parent, true);
    showToast('Đang quay lại thư mục trước...');
    return true;
  }
  return false;
}

// =========================================================================
// RENDER UI & BREADCRUMB
// =========================================================================
function renderBreadcrumb() {
  breadcrumb.innerHTML = '';

  if (state.isVault) {
    const item = document.createElement('span');
    item.className = 'crumb-item' + (!state.currentFolder ? ' active' : '');
    item.innerHTML = `
      <svg style="width:16px;height:16px;fill:#f43f5e;vertical-align:text-bottom;margin-right:6px;" viewBox="0 0 24 24"><path d="M18 8h-1V6c0-2.76-2.24-5-5-5S7 3.24 7 6v2H6c-1.1 0-2 .9-2 2v10c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V10c0-1.1-.9-2-2-2zm-6 9c-1.1 0-2-.9-2-2s.9-2 2-2 2 .9 2 2-.9 2-2 2zm3.1-9H8.9V6c0-1.71 1.39-3.1 3.1-3.1 1.71 0 3.1 1.39 3.1 3.1v2z"/></svg>
      <span>Thư mục bí mật</span>
    `;
    item.addEventListener('click', () => {
      if (state.currentFolder) {
        navigateToFolder('', true);
      }
    });
    breadcrumb.appendChild(item);

    if (state.currentFolder) {
      const parts = state.currentFolder.split('/').filter(Boolean);
      let accum = '';
      parts.forEach((p, idx) => {
        accum = accum ? `${accum}/${p}` : p;
        const pathRef = accum;
        const isLast = idx === parts.length - 1;

        const sep = document.createElement('span');
        sep.className = 'crumb-sep';
        sep.textContent = '>';
        breadcrumb.appendChild(sep);

        const crumb = document.createElement('span');
        crumb.className = 'crumb-item' + (isLast ? ' active' : '');
        crumb.textContent = p;
        if (!isLast) {
          crumb.addEventListener('click', () => {
            navigateToFolder(pathRef, true);
          });
        }
        breadcrumb.appendChild(crumb);
      });
    }
    return;
  }

  if (state.isTrash) {
    const item = document.createElement('span');
    item.className = 'crumb-item active';
    item.textContent = 'Thùng rác';
    breadcrumb.appendChild(item);
    return;
  }

  if (state.currentFilter !== 'all') {
    const titles = {
      video: 'Video & Phim',
      image: 'Hình ảnh & Ảnh',
      audio: 'Âm thanh & Nhạc',
      document: 'Tài liệu'
    };
    const item = document.createElement('span');
    item.className = 'crumb-item active';
    item.textContent = titles[state.currentFilter] || 'Bộ lọc';
    breadcrumb.appendChild(item);
    return;
  }

  if (state.searchQuery) {
    const item = document.createElement('span');
    item.className = 'crumb-item active';
    item.textContent = `Kết quả tìm kiếm cho "${state.searchQuery}"`;
    breadcrumb.appendChild(item);
    return;
  }

  // Home Root
  const rootItem = document.createElement('span');
  rootItem.className = 'crumb-item' + (!state.currentFolder ? ' active' : '');
  rootItem.textContent = 'Drive của tôi';
  rootItem.addEventListener('click', () => {
    navigateToFolder('', true);
  });

  // Hỗ trợ thả tệp về thư mục gốc Drive
  if (state.currentFolder && !state.isTrash) {
    rootItem.addEventListener('dragover', (e) => {
      if (e.dataTransfer.types.includes('application/json')) {
        e.preventDefault();
        e.dataTransfer.dropEffect = 'move';
        rootItem.classList.add('is-drop-target');
      }
    });
    rootItem.addEventListener('dragleave', () => {
      rootItem.classList.remove('is-drop-target');
    });
    rootItem.addEventListener('drop', async (e) => {
      e.preventDefault();
      rootItem.classList.remove('is-drop-target');
      try {
        const raw = e.dataTransfer.getData('application/json');
        if (raw) {
          const parsed = JSON.parse(raw);
          if (parsed && parsed.type === 'nas-items' && Array.isArray(parsed.paths)) {
            await executeMove(parsed.paths, '');
          }
        }
      } catch (_) {}
    });
  }

  breadcrumb.appendChild(rootItem);

  if (!state.currentFolder) return;

  const parts = state.currentFolder.split('/').filter(Boolean);
  let accum = '';
  parts.forEach((p, idx) => {
    accum = accum ? `${accum}/${p}` : p;
    const pathRef = accum;
    const isLast = idx === parts.length - 1;

    const sep = document.createElement('span');
    sep.className = 'crumb-sep';
    sep.textContent = '>';
    breadcrumb.appendChild(sep);

    const crumb = document.createElement('span');
    crumb.className = 'crumb-item' + (isLast ? ' active' : '');
    crumb.textContent = p;
    if (!isLast) {
      crumb.addEventListener('click', () => {
        navigateToFolder(pathRef, true);
      });

      crumb.addEventListener('dragover', (e) => {
        if (e.dataTransfer.types.includes('application/json')) {
          e.preventDefault();
          e.dataTransfer.dropEffect = 'move';
          crumb.classList.add('is-drop-target');
        }
      });
      crumb.addEventListener('dragleave', () => {
        crumb.classList.remove('is-drop-target');
      });
      crumb.addEventListener('drop', async (e) => {
        e.preventDefault();
        crumb.classList.remove('is-drop-target');
        try {
          const raw = e.dataTransfer.getData('application/json');
          if (raw) {
            const parsed = JSON.parse(raw);
            if (parsed && parsed.type === 'nas-items' && Array.isArray(parsed.paths)) {
              const validPaths = parsed.paths.filter(p => p !== pathRef && !pathRef.startsWith(p + '/'));
              if (validPaths.length > 0) {
                await executeMove(validPaths, pathRef);
              }
            }
          }
        } catch (_) {}
      });
    }
    breadcrumb.appendChild(crumb);
  });
}

let currentFilteredFiles = [];
let renderedFileCount = 0;
const FILE_CHUNK_SIZE = 80;
let infiniteScrollObserver = null;

function setupInfiniteScroll() {
  const sentinel = document.querySelector('#infiniteScrollSentinel');
  if (!sentinel) return;

  if (infiniteScrollObserver) infiniteScrollObserver.disconnect();

  infiniteScrollObserver = new IntersectionObserver((entries) => {
    for (const entry of entries) {
      if (entry.isIntersecting && renderedFileCount < currentFilteredFiles.length) {
        renderNextFileChunk();
      }
    }
  }, { root: fileViewContainer, rootMargin: '600px' });

  infiniteScrollObserver.observe(sentinel);
}

// Bộ lắng nghe cuộn dự phòng để luôn nạp mượt mà trên mọi thiết bị
let scrollThrottleTimer = null;
if (fileViewContainer) {
  fileViewContainer.addEventListener('scroll', () => {
    if (scrollThrottleTimer) return;
    scrollThrottleTimer = setTimeout(() => {
      scrollThrottleTimer = null;
      if (renderedFileCount < currentFilteredFiles.length) {
        const remaining = fileViewContainer.scrollHeight - fileViewContainer.scrollTop - fileViewContainer.clientHeight;
        if (remaining < 800) {
          renderNextFileChunk();
        }
      }
    }, 100);
  }, { passive: true });
}

function renderNextFileChunk() {
  if (renderedFileCount >= currentFilteredFiles.length) return;
  const nextSlice = currentFilteredFiles.slice(renderedFileCount, renderedFileCount + FILE_CHUNK_SIZE);
  if (nextSlice.length === 0) return;

  const fragment = document.createDocumentFragment();
  for (const item of nextSlice) {
    fragment.appendChild(createItemCard(item));
  }
  filesGrid.appendChild(fragment);
  renderedFileCount += nextSlice.length;
}

function renderItems() {
  const filesHeader = filesSection.querySelector('.section-header-row');
  if (filesHeader) filesHeader.hidden = false;

  const folders = state.items.filter(i => i.isFolder);
  currentFilteredFiles = state.items.filter(i => !i.isFolder);

  foldersSection.hidden = folders.length === 0;
  filesSection.hidden = currentFilteredFiles.length === 0;
  emptyState.hidden = state.items.length > 0;

  // Render Folders (dùng DocumentFragment)
  foldersGrid.replaceChildren();
  if (folders.length > 0) {
    const fFrag = document.createDocumentFragment();
    for (const item of folders) {
      fFrag.appendChild(createItemCard(item));
    }
    foldersGrid.appendChild(fFrag);
  }

  // Render tệp theo từng khối 80 mục để không làm đông cứng trình duyệt
  filesGrid.replaceChildren();
  renderedFileCount = 0;
  if (currentFilteredFiles.length > 0) {
    renderNextFileChunk();
  }

  setupInfiniteScroll();
}

function createItemCard(item) {
  if (item.isVault) {
    item.path = item.path || item.id;
    item.formattedSize = item.formattedSize || formatBytes(item.size || 0);
    item.formattedDate = item.formattedDate || (item.createdAt ? item.createdAt.split('T')[0] : '');
  }

  const card = document.createElement('div');
  card.className = 'drive-card' + (selectedPaths.has(item.path) ? ' is-selected' : '');
  card.dataset.path = item.path;
  card.dataset.isFolder = String(item.isFolder);

  let previewHtml = '';
  const isVideo = item.type === 'video';
  const isImage = item.type === 'image';

  if (item.isVault) {
    if (isImage) {
      previewHtml = `<div class="card-preview"><img src="${api(`/api/vault/stream?id=${encodeURIComponent(item.id)}`)}" loading="lazy" alt="${escapeHtml(item.name)}"></div>`;
    } else if (isVideo) {
      previewHtml = `
        <div class="card-preview has-video-thumb">
          <img class="video-thumb-img" src="${api(`/api/thumbnail?path=${encodeURIComponent(item.path)}`)}" loading="lazy" alt="${escapeHtml(item.name)}" onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';">
          <div class="video-fallback-icon" style="display:none;">${SVG_ICONS.video}</div>
          <div class="video-play-badge" title="Phát video">
            <svg viewBox="0 0 24 24"><path d="M8 5v14l11-7z"/></svg>
          </div>
        </div>
      `;
    } else {
      previewHtml = `<div class="card-preview">${SVG_ICONS[item.type] || SVG_ICONS.other}</div>`;
    }
  } else if (isImage) {
    previewHtml = `<div class="card-preview"><img src="${api(`/api/stream?path=${encodeURIComponent(item.path)}`)}" loading="lazy" alt="${escapeHtml(item.name)}"></div>`;
  } else if (isVideo) {
    previewHtml = `
      <div class="card-preview has-video-thumb">
        <img class="video-thumb-img" src="${api(`/api/thumbnail?path=${encodeURIComponent(item.path)}`)}" loading="lazy" alt="${escapeHtml(item.name)}" onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';">
        <div class="video-fallback-icon" style="display:none;">${SVG_ICONS.video}</div>
        <div class="video-play-badge" title="Phát video">
          <svg viewBox="0 0 24 24"><path d="M8 5v14l11-7z"/></svg>
        </div>
      </div>
    `;
  } else {
    previewHtml = `<div class="card-preview">${SVG_ICONS[item.type] || SVG_ICONS.other}</div>`;
  }

  const vaultBadge = item.isVault
    ? `<span style="font-size:10px;font-weight:700;color:#f43f5e;background:rgba(244,63,94,0.15);padding:1px 5px;border-radius:3px;margin-left:4px;">🔒 BÍ MẬT</span>`
    : '';

  card.innerHTML = `
    <div class="card-select-trigger" title="Chọn">
      <input type="checkbox" class="card-checkbox" ${selectedPaths.has(item.path) ? 'checked' : ''}>
    </div>
    ${previewHtml}
    <div class="card-body">
      <div class="card-name-row">
        <span class="card-name" title="${escapeHtml(item.name)}">${escapeHtml(item.name)}</span>
        ${vaultBadge}
        <button type="button" class="card-menu-btn" title="Tùy chọn">⋮</button>
      </div>
      <div class="card-meta">
        <span>${item.formattedSize}</span>
        <span>${item.formattedDate.split(' ')[0]}</span>
      </div>
    </div>
  `;

  if (!item.isVault) {
    card.draggable = true;
    card.addEventListener('dragstart', (e) => {
      const paths = selectedPaths.has(item.path) && selectedPaths.size > 1
        ? Array.from(selectedPaths)
        : [item.path];
      e.dataTransfer.setData('application/json', JSON.stringify({ type: 'nas-items', paths }));
      e.dataTransfer.effectAllowed = 'move';
    });
  }

  // Nếu là thư mục: Hỗ trợ thả tệp trực tiếp vào thư mục (Drag and drop into folder)
  if (item.isFolder && !state.isTrash) {
    card.addEventListener('dragover', (e) => {
      if (e.dataTransfer.types.includes('application/json')) {
        e.preventDefault();
        e.dataTransfer.dropEffect = 'move';
        card.classList.add('is-drop-target');
      }
    });
    card.addEventListener('dragleave', (e) => {
      if (!card.contains(e.relatedTarget)) {
        card.classList.remove('is-drop-target');
      }
    });
    card.addEventListener('drop', async (e) => {
      e.preventDefault();
      card.classList.remove('is-drop-target');
      try {
        const raw = e.dataTransfer.getData('application/json');
        if (raw) {
          const parsed = JSON.parse(raw);
          if (parsed && parsed.type === 'nas-items' && Array.isArray(parsed.paths)) {
            const validPaths = parsed.paths.filter(p => p !== item.path && !item.path.startsWith(p + '/'));
            if (validPaths.length > 0) {
              await executeMove(validPaths, item.path);
            }
          }
        }
      } catch (_) {}
    });
  }

  return card;
}

function toggleItemSelection(item, card) {
  const checkbox = card.querySelector('.card-checkbox');
  if (selectedPaths.has(item.path)) {
    selectedPaths.delete(item.path);
    card.classList.remove('is-selected');
    if (checkbox) checkbox.checked = false;
  } else {
    selectedPaths.add(item.path);
    card.classList.add('is-selected');
    if (checkbox) checkbox.checked = true;
  }
  syncSelection();
}

function setupGridEventDelegation(gridElement) {
  gridElement.addEventListener('click', (e) => {
    const card = e.target.closest('.drive-card');
    if (!card) return;
    const path = card.dataset.path;
    const item = state.items.find(i => i.path === path);
    if (!item) return;

    if (e.target.closest('.card-select-trigger')) {
      return;
    }
    if (e.target.closest('.card-menu-btn')) {
      e.stopPropagation();
      showContextMenu(e, item);
      return;
    }
    if (e.ctrlKey || e.metaKey) {
      toggleItemSelection(item, card);
      return;
    }
    openItem(item, card);
  });

  // Chuột phải (Context Menu) trực tiếp trên tệp / thư mục
  gridElement.addEventListener('contextmenu', (e) => {
    const card = e.target.closest('.drive-card');
    if (!card) return;
    const path = card.dataset.path;
    const item = state.items.find(i => i.path === path);
    if (!item) return;
    e.preventDefault();
    e.stopPropagation();
    showContextMenu(e, item);
  });

  gridElement.addEventListener('change', (e) => {
    if (e.target.classList.contains('card-checkbox')) {
      const card = e.target.closest('.drive-card');
      if (!card) return;
      const path = card.dataset.path;
      const item = state.items.find(i => i.path === path);
      if (!item) return;

      if (e.target.checked) {
        selectedPaths.add(item.path);
        card.classList.add('is-selected');
      } else {
        selectedPaths.delete(item.path);
        card.classList.remove('is-selected');
      }
      syncSelection();
    }
  });
}

const TEXT_CODE_EXTS = ['.txt', '.md', '.json', '.csv', '.log', '.js', '.mjs', '.py', '.html', '.css', '.sh', '.yml', '.yaml', '.xml', '.ini', '.env', '.conf'];

function openItem(item, cardEl = null) {
  if (item.isFolder) {
    if (cardEl) cardEl.classList.add('is-opening');

    // Reset bộ lọc & tìm kiếm để nạp trực tiếp toàn bộ nội dung thư mục
    if (state.currentFilter !== 'all' || state.searchQuery) {
      state.currentFilter = 'all';
      state.searchQuery = '';
      if (searchInput) searchInput.value = '';
      if (searchClearBtn) searchClearBtn.hidden = true;
      document.querySelectorAll('.sidebar-nav .nav-link').forEach(l => {
        l.classList.toggle('active', l.dataset.filter === 'all' && !l.dataset.trash);
      });
    }

    navigateToFolder(item.path, true);
    return;
  }

  const ext = (item.ext || (item.name && item.name.includes('.') ? item.name.substring(item.name.lastIndexOf('.')) : '')).toLowerCase();

  if (item.isVault) {
    if (item.type === 'video') {
      openVideoPlayer(item);
    } else if (item.type === 'audio') {
      openAudioPlayer(item);
    } else if (item.type === 'image') {
      openImageLightbox(item);
    } else if (ext === '.pdf') {
      openPdfViewer(item);
    } else if (TEXT_CODE_EXTS.includes(ext)) {
      openCodeViewer(item);
    } else {
      window.open(api(`/api/vault/download?id=${encodeURIComponent(item.id)}`), '_blank');
    }
    return;
  }

  if (item.type === 'video') {
    openVideoPlayer(item);
  } else if (item.type === 'audio') {
    openAudioPlayer(item);
  } else if (item.type === 'image') {
    openImageLightbox(item);
  } else if (ext === '.pdf') {
    openPdfViewer(item);
  } else if (TEXT_CODE_EXTS.includes(ext)) {
    openCodeViewer(item);
  } else {
    // Download other files
    window.open(api(`/api/download?path=${encodeURIComponent(item.path)}`), '_blank');
  }
}

// =========================================================================
// SELECTION BAR LOGIC
// =========================================================================
function syncSelection() {
  const count = selectedPaths.size;
  if (count === 0) {
    selectionBar.hidden = true;
    document.body.classList.remove('has-selection');
    return;
  }

  selectionBar.hidden = false;
  document.body.classList.add('has-selection');
  selectionCountText.textContent = `Đã chọn ${count} mục`;

  if (state.isTrash) {
    bulkTrashBtnText.textContent = 'Xóa vĩnh viễn';
    bulkRestoreBtn.hidden = false;
    emptyTrashBtn.hidden = false;
    if (bulkMoveBtn) bulkMoveBtn.hidden = true;
    if (bulkVaultBtn) bulkVaultBtn.hidden = true;
    if (bulkExportVaultBtn) bulkExportVaultBtn.hidden = true;
  } else if (state.isVault) {
    bulkTrashBtnText.textContent = 'Xóa khỏi Thư mục bí mật';
    bulkRestoreBtn.hidden = true;
    emptyTrashBtn.hidden = true;
    if (bulkMoveBtn) bulkMoveBtn.hidden = false;
    if (bulkVaultBtn) bulkVaultBtn.hidden = true;
    if (bulkExportVaultBtn) bulkExportVaultBtn.hidden = false;
  } else {
    bulkTrashBtnText.textContent = 'Chuyển vào Thùng rác';
    bulkRestoreBtn.hidden = true;
    emptyTrashBtn.hidden = true;
    if (bulkMoveBtn) bulkMoveBtn.hidden = false;
    if (bulkVaultBtn) bulkVaultBtn.hidden = false;
    if (bulkExportVaultBtn) bulkExportVaultBtn.hidden = true;
  }
}

clearSelectionBtn.addEventListener('click', () => {
  selectedPaths.clear();
  document.querySelectorAll('.drive-card.is-selected').forEach(c => c.classList.remove('is-selected'));
  document.querySelectorAll('.card-checkbox:checked').forEach(cb => cb.checked = false);
  syncSelection();
});

bulkDownloadBtn.addEventListener('click', () => {
  for (const path of selectedPaths) {
    const item = state.items.find(i => i.path === path);
    if (item && !item.isFolder) {
      if (item.isVault) {
        window.open(api(`/api/vault/download?id=${encodeURIComponent(item.id || item.path)}`), '_blank');
      } else {
        window.open(api(`/api/download?path=${encodeURIComponent(path)}`), '_blank');
      }
    }
  }
});

bulkTrashBtn.addEventListener('click', async () => {
  if (selectedPaths.size === 0 || isBusy) return;
  const paths = Array.from(selectedPaths);

  if (state.isVault) {
    if (!confirm(`Xóa vĩnh viễn ${paths.length} mục đã chọn khỏi Thư mục bí mật? Thao tác không thể hoàn tác.`)) return;
    isBusy = true;
    try {
      for (const id of paths) {
        await authFetch(api('/api/vault/delete'), {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ id })
        });
      }
      selectedPaths.clear();
      loadFiles();
      fetchStorage();
    } catch (err) {
      alert('Lỗi xóa tệp: ' + err.message);
    } finally {
      isBusy = false;
    }
    return;
  }

  const msg = state.isTrash
    ? `Xóa vĩnh viễn ${paths.length} mục đã chọn? Thao tác này không thể hoàn tác.`
    : `Chuyển ${paths.length} mục đã chọn vào Thùng rác?`;

  if (!confirm(msg)) return;

  isBusy = true;
  try {
    const res = await authFetch(api('/api/delete'), {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ paths })
    });
    const data = await res.json();
    if (data.ok) {
      selectedPaths.clear();
      loadFiles();
      fetchStorage();
    } else {
      alert(data.error || 'Lỗi xóa tệp');
    }
  } catch (err) {
    alert('Lỗi kết nối: ' + err.message);
  } finally {
    isBusy = false;
  }
});

bulkRestoreBtn.addEventListener('click', async () => {
  if (selectedPaths.size === 0 || isBusy) return;
  const paths = Array.from(selectedPaths);
  isBusy = true;
  try {
    const res = await authFetch(api('/api/restore'), {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ paths })
    });
    const data = await res.json();
    if (data.ok) {
      selectedPaths.clear();
      loadFiles();
      fetchStorage();
    } else {
      alert(data.error || 'Lỗi khôi phục');
    }
  } catch (err) {
    alert('Lỗi kết nối: ' + err.message);
  } finally {
    isBusy = false;
  }
});

emptyTrashBtn.addEventListener('click', async () => {
  if (!confirm('Dọn sạch TẤT CẢ các tệp trong Thùng rác? Hành động này sẽ giải phóng dung lượng và không thể hoàn tác.')) return;
  isBusy = true;
  try {
    const res = await authFetch(api('/api/empty-trash'), { method: 'POST' });
    const data = await res.json();
    if (data.ok) {
      selectedPaths.clear();
      loadFiles();
      fetchStorage();
    } else {
      alert(data.error || 'Lỗi dọn thùng rác');
    }
  } catch (err) {
    alert('Lỗi kết nối: ' + err.message);
  } finally {
    isBusy = false;
  }
});

// =========================================================================
// VIDEO PLAYER (RFC 7233 RANGE 206)
// =========================================================================
function openVideoPlayer(item) {
  if (item.isVault) {
    videoTitle.textContent = item.name;
    videoDownloadBtn.href = api(`/api/vault/download?id=${encodeURIComponent(item.id)}`);
    driveVideoPlayer.src = api(`/api/vault/stream?id=${encodeURIComponent(item.id)}`);
    videoModal.hidden = false;
    driveVideoPlayer.play().catch(() => {});
    return;
  }

  if (window.AndroidBridge && typeof window.AndroidBridge.playVideo === 'function') {
    const streamUrl = new URL(api(`/api/stream?path=${encodeURIComponent(item.path)}`), window.location.href).href;
    const downloadUrl = new URL(api(`/api/download?path=${encodeURIComponent(item.path)}`), window.location.href).href;
    window.AndroidBridge.playVideo(streamUrl, item.name, downloadUrl);
    return;
  }
  videoTitle.textContent = item.name;
  videoDownloadBtn.href = api(`/api/download?path=${encodeURIComponent(item.path)}`);
  driveVideoPlayer.src = api(`/api/stream?path=${encodeURIComponent(item.path)}`);
  videoModal.hidden = false;
  driveVideoPlayer.play().catch(() => {});
}

// =========================================================================
// TOAST NOTIFICATIONS & VAULT IMPORT LOGIC
// =========================================================================
function showToast(message, type = 'info') {
  let toastContainer = document.querySelector('#toastContainer');
  if (!toastContainer) {
    toastContainer = document.createElement('div');
    toastContainer.id = 'toastContainer';
    toastContainer.className = 'toast-container';
    document.body.appendChild(toastContainer);
  }

  const toast = document.createElement('div');
  toast.className = `toast-item toast-${type}`;
  toast.innerHTML = `
    <span class="toast-icon">${type === 'success' ? '✓' : type === 'error' ? '✕' : 'ℹ'}</span>
    <span class="toast-message">${escapeHtml(message)}</span>
  `;
  toastContainer.appendChild(toast);

  setTimeout(() => {
    toast.classList.add('toast-show');
  }, 10);

  setTimeout(() => {
    toast.classList.remove('toast-show');
    setTimeout(() => toast.remove(), 300);
  }, 4000);
}

let pendingVaultImportPaths = null;

async function moveToVault(paths = []) {
  if (!paths || paths.length === 0) return;
  const count = paths.length;
  const nameDesc = count === 1 ? `"${paths[0].split('/').pop()}"` : `${count} mục`;

  if (!confirm(`Bạn có chắc muốn ẩn ${nameDesc} vào Thư mục bí mật?\nTệp/thư mục gốc sẽ được ẩn bảo mật, chỉ truy cập được khi mở khóa.`)) {
    return;
  }

  try {
    const res = await authFetch(api('/api/vault/status'));
    const status = await res.json();

    if (!status.isConfigured) {
      pendingVaultImportPaths = paths;
      openVaultUnlockModal(false);
      if (vaultModalDesc) {
        vaultModalDesc.textContent = `Để chuyển ${nameDesc} vào Thư mục bí mật, vui lòng tạo mật khẩu bảo vệ riêng trước.`;
      }
      return;
    }

    if (!status.isUnlocked) {
      pendingVaultImportPaths = paths;
      openVaultUnlockModal(true);
      if (vaultModalDesc) {
        vaultModalDesc.textContent = `Thư mục bí mật đang khóa. Vui lòng nhập mật khẩu để chuyển ${nameDesc} vào trong.`;
      }
      return;
    }

    await executeVaultImport(paths);
  } catch (err) {
    alert('Không thể kết nối đến máy chủ: ' + err.message);
  }
}

async function executeVaultImport(paths) {
  if (!paths || paths.length === 0) return;
  const count = paths.length;

  isBusy = true;
  if (stageLoadingBar) stageLoadingBar.hidden = false;

  try {
    const res = await authFetch(api('/api/vault/import'), {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ paths })
    });
    const data = await res.json();
    if (data.ok) {
      pendingVaultImportPaths = null;
      selectedPaths.clear();
      syncSelection();
      if (data.background) {
        showToast(`🔒 ${data.message || 'Đang ẩn tệp vào Thư mục bí mật trong nền...'}`, 'info');
      } else {
        showToast(`🔒 Đã chuyển thành công ${data.imported?.length || count} mục vào Thư mục bí mật!`, 'success');
        loadFiles();
        fetchStorage();
      }
    } else {
      if (data.code === 'VAULT_LOCKED') {
        pendingVaultImportPaths = paths;
        openVaultUnlockModal(true);
      } else {
        pendingVaultImportPaths = null;
        alert(data.error || 'Lỗi chuyển tệp vào Thư mục bí mật');
      }
    }
  } catch (err) {
    pendingVaultImportPaths = null;
    alert('Lỗi: ' + err.message);
  } finally {
    isBusy = false;
    if (stageLoadingBar) stageLoadingBar.hidden = true;
  }
}

if (bulkVaultBtn) {
  bulkVaultBtn.addEventListener('click', async () => {
    if (selectedPaths.size === 0 || isBusy) return;
    const paths = Array.from(selectedPaths);
    await moveToVault(paths);
  });
}

if (bulkExportVaultBtn) {
  bulkExportVaultBtn.addEventListener('click', async () => {
    if (selectedPaths.size === 0 || isBusy) return;
    const ids = Array.from(selectedPaths);
    if (!confirm(`Đưa ${ids.length} mục từ Thư mục bí mật về lại Drive?`)) return;
    isBusy = true;
    try {
      const res = await authFetch(api('/api/vault/export'), {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ ids, targetFolder: '' })
      });
      const data = await res.json();
      if (data.ok) {
        alert(`Đã đưa ${data.exported?.length || ids.length} mục về lại Drive thành công!`);
        selectedPaths.clear();
        loadFiles();
        fetchStorage();
      } else {
        alert(data.error || 'Lỗi giải mã tệp');
      }
    } catch (err) {
      alert('Lỗi kết nối: ' + err.message);
    } finally {
      isBusy = false;
    }
  });
}

function closeVideoPlayer() {
  driveVideoPlayer.pause();
  driveVideoPlayer.removeAttribute('src');
  driveVideoPlayer.load();
  videoModal.hidden = true;
}

closeVideoBtn.addEventListener('click', closeVideoPlayer);
videoBackdrop.addEventListener('click', closeVideoPlayer);

// =========================================================================
// IMAGE LIGHTBOX
// =========================================================================
function openImageLightbox(item) {
  if (item.isVault) {
    imageTitle.textContent = item.name;
    imageDownloadBtn.href = api(`/api/vault/download?id=${encodeURIComponent(item.id)}`);
    driveImagePreview.src = api(`/api/vault/stream?id=${encodeURIComponent(item.id)}`);
    imageModal.hidden = false;
    return;
  }

  imageTitle.textContent = item.name;
  imageDownloadBtn.href = api(`/api/download?path=${encodeURIComponent(item.path)}`);
  driveImagePreview.src = api(`/api/stream?path=${encodeURIComponent(item.path)}`);
  imageModal.hidden = false;
}

function closeImageLightbox() {
  driveImagePreview.src = '';
  imageModal.hidden = true;
}

closeImageBtn.addEventListener('click', closeImageLightbox);
imageBackdrop.addEventListener('click', closeImageLightbox);

// =========================================================================
// TOAST NOTIFICATIONS
// =========================================================================
function showToast(message, duration = 3000) {
  const container = document.getElementById('toastContainer');
  if (!container) return;
  const toast = document.createElement('div');
  toast.className = 'toast-message';
  toast.innerHTML = `<svg viewBox="0 0 24 24" style="width:16px;height:16px;fill:#10b981;"><path d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z"/></svg><span>${escapeHtml(message)}</span>`;
  container.appendChild(toast);
  setTimeout(() => {
    toast.style.animation = 'toastFadeIn 0.3s cubic-bezier(0.34, 1.56, 0.64, 1) reverse forwards';
    setTimeout(() => toast.remove(), 300);
  }, duration);
}

// =========================================================================
// AUDIO PLAYER (HI-RES MUSIC & PODCAST)
// =========================================================================
const audioModal = document.getElementById('audioModal');
const audioBackdrop = document.getElementById('audioBackdrop');
const closeAudioBtn = document.getElementById('closeAudioBtn');
const driveAudioPlayer = document.getElementById('driveAudioPlayer');
const audioVinylDisc = document.getElementById('audioVinylDisc');
const audioTrackName = document.getElementById('audioTrackName');
const audioTrackMeta = document.getElementById('audioTrackMeta');
const audioCurTime = document.getElementById('audioCurTime');
const audioTotalTime = document.getElementById('audioTotalTime');
const audioSeekerBar = document.getElementById('audioSeekerBar');
const audioSeekProgress = document.getElementById('audioSeekProgress');
const audioSeekBuffer = document.getElementById('audioSeekBuffer');
const audioPlayBtn = document.getElementById('audioPlayBtn');
const audioPlayIcon = document.getElementById('audioPlayIcon');
const audioPauseIcon = document.getElementById('audioPauseIcon');
const audioPrevBtn = document.getElementById('audioPrevBtn');
const audioNextBtn = document.getElementById('audioNextBtn');
const audioRepeatBtn = document.getElementById('audioRepeatBtn');
const audioSpeedBtn = document.getElementById('audioSpeedBtn');
const audioVolumeSlider = document.getElementById('audioVolumeSlider');
const audioMuteBtn = document.getElementById('audioMuteBtn');
const audioDownloadBtn = document.getElementById('audioDownloadBtn');

let currentAudioList = [];
let currentAudioIndex = -1;
let audioRepeat = false;
const audioSpeedList = [1.0, 1.25, 1.5, 2.0, 0.75];
let audioSpeedIndex = 0;

function formatAudioTime(seconds) {
  if (isNaN(seconds) || seconds < 0) return '00:00';
  const mins = Math.floor(seconds / 60);
  const secs = Math.floor(seconds % 60);
  return `${mins < 10 ? '0' : ''}${mins}:${secs < 10 ? '0' : ''}${secs}`;
}

function openAudioPlayer(item) {
  closeContextMenu();
  currentAudioList = state.items.filter(i => !i.isFolder && i.type === 'audio');
  currentAudioIndex = currentAudioList.findIndex(i => (item.isVault ? i.id === item.id : i.path === item.path));
  if (currentAudioIndex === -1) {
    currentAudioList = [item];
    currentAudioIndex = 0;
  }
  loadAudioTrack(currentAudioList[currentAudioIndex]);
  audioModal.hidden = false;
}

function loadAudioTrack(item) {
  if (!item) return;
  audioTrackName.textContent = item.name;
  audioTrackName.title = item.name;
  audioTrackMeta.textContent = `${item.formattedSize || ''} • NAS Hi-Res Audio`;

  const streamUrl = item.isVault
    ? api(`/api/vault/stream?id=${encodeURIComponent(item.id)}`)
    : api(`/api/stream?path=${encodeURIComponent(item.path)}`);
  const downloadUrl = item.isVault
    ? api(`/api/vault/download?id=${encodeURIComponent(item.id)}`)
    : api(`/api/download?path=${encodeURIComponent(item.path)}`);

  audioDownloadBtn.href = downloadUrl;
  audioDownloadBtn.download = item.name;

  driveAudioPlayer.src = streamUrl;
  driveAudioPlayer.playbackRate = audioSpeedList[audioSpeedIndex];
  driveAudioPlayer.play().then(() => {
    setAudioPlayingState(true);
  }).catch(() => {
    setAudioPlayingState(false);
  });
}

function setAudioPlayingState(isPlaying) {
  if (isPlaying) {
    audioPlayIcon.hidden = true;
    audioPauseIcon.hidden = false;
    audioVinylDisc.classList.add('is-playing');
  } else {
    audioPlayIcon.hidden = false;
    audioPauseIcon.hidden = true;
    audioVinylDisc.classList.remove('is-playing');
  }
}

function toggleAudioPlay() {
  if (driveAudioPlayer.paused) {
    driveAudioPlayer.play().then(() => setAudioPlayingState(true)).catch(() => {});
  } else {
    driveAudioPlayer.pause();
    setAudioPlayingState(false);
  }
}

function playPrevAudio() {
  if (currentAudioList.length <= 1) return;
  currentAudioIndex = (currentAudioIndex - 1 + currentAudioList.length) % currentAudioList.length;
  loadAudioTrack(currentAudioList[currentAudioIndex]);
}

function playNextAudio() {
  if (currentAudioList.length <= 1) return;
  currentAudioIndex = (currentAudioIndex + 1) % currentAudioList.length;
  loadAudioTrack(currentAudioList[currentAudioIndex]);
}

function closeAudioPlayer() {
  driveAudioPlayer.pause();
  driveAudioPlayer.src = '';
  setAudioPlayingState(false);
  audioModal.hidden = true;
}

driveAudioPlayer.addEventListener('timeupdate', () => {
  const cur = driveAudioPlayer.currentTime;
  const dur = driveAudioPlayer.duration;
  audioCurTime.textContent = formatAudioTime(cur);
  if (!isNaN(dur) && dur > 0) {
    audioTotalTime.textContent = formatAudioTime(dur);
    const pct = (cur / dur) * 100;
    audioSeekProgress.style.width = `${pct}%`;
  }
  if (driveAudioPlayer.buffered.length > 0 && !isNaN(dur) && dur > 0) {
    const bufEnd = driveAudioPlayer.buffered.end(driveAudioPlayer.buffered.length - 1);
    audioSeekBuffer.style.width = `${(bufEnd / dur) * 100}%`;
  }
});

driveAudioPlayer.addEventListener('ended', () => {
  if (audioRepeat) {
    driveAudioPlayer.currentTime = 0;
    driveAudioPlayer.play();
  } else if (currentAudioList.length > 1) {
    playNextAudio();
  } else {
    setAudioPlayingState(false);
  }
});

audioPlayBtn.addEventListener('click', toggleAudioPlay);
audioPrevBtn.addEventListener('click', playPrevAudio);
audioNextBtn.addEventListener('click', playNextAudio);

audioSeekerBar.addEventListener('click', (e) => {
  const rect = audioSeekerBar.getBoundingClientRect();
  const clickX = e.clientX - rect.left;
  const pct = Math.max(0, Math.min(1, clickX / rect.width));
  if (driveAudioPlayer.duration) {
    driveAudioPlayer.currentTime = pct * driveAudioPlayer.duration;
  }
});

audioRepeatBtn.addEventListener('click', () => {
  audioRepeat = !audioRepeat;
  audioRepeatBtn.classList.toggle('is-active', audioRepeat);
});

audioSpeedBtn.addEventListener('click', () => {
  audioSpeedIndex = (audioSpeedIndex + 1) % audioSpeedList.length;
  const spd = audioSpeedList[audioSpeedIndex];
  audioSpeedBtn.textContent = `${spd.toFixed(1)}x`;
  driveAudioPlayer.playbackRate = spd;
});

audioVolumeSlider.addEventListener('input', (e) => {
  driveAudioPlayer.volume = parseFloat(e.target.value);
});

audioMuteBtn.addEventListener('click', () => {
  driveAudioPlayer.muted = !driveAudioPlayer.muted;
  document.getElementById('audioVolHighIcon').hidden = driveAudioPlayer.muted;
  document.getElementById('audioVolMuteIcon').hidden = !driveAudioPlayer.muted;
});

closeAudioBtn.addEventListener('click', closeAudioPlayer);
audioBackdrop.addEventListener('click', closeAudioPlayer);

// =========================================================================
// PDF VIEWER
// =========================================================================
const pdfModal = document.getElementById('pdfModal');
const pdfBackdrop = document.getElementById('pdfBackdrop');
const closePdfBtn = document.getElementById('closePdfBtn');
const pdfTitle = document.getElementById('pdfTitle');
const pdfDownloadBtn = document.getElementById('pdfDownloadBtn');
const pdfOpenTabBtn = document.getElementById('pdfOpenTabBtn');
const drivePdfFrame = document.getElementById('drivePdfFrame');

function openPdfViewer(item) {
  closeContextMenu();
  const pdfUrl = item.isVault
    ? api(`/api/vault/download?id=${encodeURIComponent(item.id)}&inline=1`)
    : api(`/api/download?path=${encodeURIComponent(item.path)}&inline=1`);
  const dlUrl = item.isVault
    ? api(`/api/vault/download?id=${encodeURIComponent(item.id)}`)
    : api(`/api/download?path=${encodeURIComponent(item.path)}`);

  pdfTitle.textContent = item.name;
  pdfDownloadBtn.href = dlUrl;
  pdfDownloadBtn.download = item.name;
  pdfOpenTabBtn.href = pdfUrl;
  drivePdfFrame.src = pdfUrl;
  pdfModal.hidden = false;
}

function closePdfViewer() {
  drivePdfFrame.src = 'about:blank';
  pdfModal.hidden = true;
}

closePdfBtn.addEventListener('click', closePdfViewer);
pdfBackdrop.addEventListener('click', closePdfViewer);

// =========================================================================
// CODE & TEXT VIEWER
// =========================================================================
const codeModal = document.getElementById('codeModal');
const codeBackdrop = document.getElementById('codeBackdrop');
const closeCodeBtn = document.getElementById('closeCodeBtn');
const codeTitle = document.getElementById('codeTitle');
const codeLanguageBadge = document.getElementById('codeLanguageBadge');
const codeMetaInfo = document.getElementById('codeMetaInfo');
const codeViewerContent = document.getElementById('codeViewerContent');
const codeLoading = document.getElementById('codeLoading');
const copyCodeBtn = document.getElementById('copyCodeBtn');
const codeDownloadBtn = document.getElementById('codeDownloadBtn');

async function openCodeViewer(item) {
  closeContextMenu();
  const ext = (item.ext || '').replace('.', '').toUpperCase() || 'TXT';
  codeTitle.textContent = item.name;
  codeLanguageBadge.textContent = ext;
  codeMetaInfo.textContent = item.formattedSize || '';
  codeViewerContent.textContent = '';
  codeLoading.hidden = false;
  codeModal.hidden = false;

  const dlUrl = item.isVault
    ? api(`/api/vault/download?id=${encodeURIComponent(item.id)}`)
    : api(`/api/download?path=${encodeURIComponent(item.path)}`);
  codeDownloadBtn.href = dlUrl;
  codeDownloadBtn.download = item.name;

  try {
    const rawUrl = item.isVault
      ? api(`/api/vault/download?id=${encodeURIComponent(item.id)}&inline=1`)
      : api(`/api/download?path=${encodeURIComponent(item.path)}&inline=1`);
    const res = await authFetch(rawUrl);
    if (!res.ok) throw new Error(`HTTP ${res.status}`);
    const text = await res.text();
    codeLoading.hidden = true;
    codeViewerContent.textContent = text;
    const lines = text.split('\n').length;
    codeMetaInfo.textContent = `${lines} dòng • ${item.formattedSize || ''}`;
  } catch (err) {
    codeLoading.hidden = true;
    codeViewerContent.textContent = `Lỗi đọc nội dung tệp: ${err.message}`;
  }
}

function closeCodeViewer() {
  codeModal.hidden = true;
  codeViewerContent.textContent = '';
}

copyCodeBtn.addEventListener('click', () => {
  const text = codeViewerContent.textContent || '';
  navigator.clipboard.writeText(text).then(() => {
    showToast('Đã sao chép toàn bộ nội dung mã nguồn!');
  }).catch(() => {
    showToast('Không thể sao chép');
  });
});

closeCodeBtn.addEventListener('click', closeCodeViewer);
codeBackdrop.addEventListener('click', closeCodeViewer);

// =========================================================================
// FILE PROPERTIES / DETAILS MODAL
// =========================================================================
const fileDetailsModal = document.getElementById('fileDetailsModal');
const closeFileDetailsBtn = document.getElementById('closeFileDetailsBtn');
const fdName = document.getElementById('fdName');
const fdType = document.getElementById('fdType');
const fdSize = document.getElementById('fdSize');
const fdPath = document.getElementById('fdPath');
const fdDate = document.getElementById('fdDate');
const fdDownloadUrlInput = document.getElementById('fdDownloadUrlInput');
const fdStreamUrlInput = document.getElementById('fdStreamUrlInput');
const fdCopyDownloadBtn = document.getElementById('fdCopyDownloadBtn');
const fdCopyStreamBtn = document.getElementById('fdCopyStreamBtn');
const fdStreamBlock = document.getElementById('fdStreamBlock');

function openFileDetails(item) {
  closeContextMenu();
  fdName.textContent = item.name;
  fdType.textContent = item.isFolder ? 'Thư mục' : (item.type ? item.type.toUpperCase() : 'Tệp tin');
  fdSize.textContent = item.isFolder ? '--' : `${item.formattedSize} (${(item.size || 0).toLocaleString()} bytes)`;
  fdPath.textContent = item.path || '/';
  fdDate.textContent = item.formattedDate || '--';

  const token = state.token || '';
  const tokenParam = token ? `&token=${encodeURIComponent(token)}` : '';
  const origin = window.location.origin;

  const dlUrl = item.isVault
    ? `${origin}${api(`/api/vault/download?id=${encodeURIComponent(item.id)}`)}${tokenParam}`
    : `${origin}${api(`/api/download?path=${encodeURIComponent(item.path)}`)}${tokenParam}`;
  fdDownloadUrlInput.value = dlUrl;

  if (item.type === 'video' || item.type === 'audio') {
    fdStreamBlock.hidden = false;
    const streamUrl = item.isVault
      ? `${origin}${api(`/api/vault/stream?id=${encodeURIComponent(item.id)}`)}${tokenParam}`
      : `${origin}${api(`/api/stream?path=${encodeURIComponent(item.path)}`)}${tokenParam}`;
    fdStreamUrlInput.value = streamUrl;
  } else {
    fdStreamBlock.hidden = true;
  }

  fileDetailsModal.hidden = false;
}

closeFileDetailsBtn.addEventListener('click', () => {
  fileDetailsModal.hidden = true;
});

fdCopyDownloadBtn.addEventListener('click', () => {
  navigator.clipboard.writeText(fdDownloadUrlInput.value).then(() => {
    showToast('Đã sao chép liên kết tải xuống!');
  });
});

fdCopyStreamBtn.addEventListener('click', () => {
  navigator.clipboard.writeText(fdStreamUrlInput.value).then(() => {
    showToast('Đã sao chép liên kết phát trực tiếp (Stream URL)!');
  });
});

// =========================================================================
// KEYBOARD SHORTCUTS (ESC, SPACEBAR PREVIEW, CTRL+A, DELETE)
// =========================================================================
window.addEventListener('keydown', (e) => {
  const isInput = ['INPUT', 'TEXTAREA'].includes(document.activeElement?.tagName);

  // 1. Phím Escape: Đóng các modal đang mở hoặc hủy chọn
  if (e.key === 'Escape') {
    if (!videoModal.hidden) closeVideoPlayer();
    if (!audioModal.hidden) closeAudioPlayer();
    if (!imageModal.hidden) closeImageLightbox();
    if (!pdfModal.hidden) closePdfViewer();
    if (!codeModal.hidden) closeCodeViewer();
    if (!fileDetailsModal.hidden) fileDetailsModal.hidden = true;
    if (moveModal && !moveModal.hidden) moveModal.hidden = true;
    if (!newFolderModal.hidden) newFolderModal.hidden = true;
    if (!renameModal.hidden) renameModal.hidden = true;
    if (selectedPaths.size > 0) {
      selectedPaths.clear();
      document.querySelectorAll('.drive-card.is-selected').forEach(c => c.classList.remove('is-selected'));
      document.querySelectorAll('.card-checkbox:checked').forEach(cb => cb.checked = false);
      syncSelection();
    }
    closeContextMenu();
    return;
  }

  if (isInput) return;

  // 2. Phím Space: Quick Look / Xem trước nhanh tệp đang chọn
  if (e.key === ' ' || e.code === 'Space') {
    if (selectedPaths.size === 1) {
      e.preventDefault();
      const path = Array.from(selectedPaths)[0];
      const item = state.items.find(i => i.path === path || i.id === path);
      if (item) openItem(item);
      return;
    }
    if (!audioModal.hidden) {
      e.preventDefault();
      toggleAudioPlay();
      return;
    }
  }

  // 3. Phím Ctrl + A: Chọn tất cả tệp trong thư mục hiện tại
  if ((e.ctrlKey || e.metaKey) && (e.key === 'a' || e.key === 'A')) {
    e.preventDefault();
    currentFilteredFiles.forEach(f => selectedPaths.add(f.path));
    document.querySelectorAll('.drive-card').forEach(c => {
      c.classList.add('is-selected');
      const cb = c.querySelector('.card-checkbox');
      if (cb) cb.checked = true;
    });
    syncSelection();
    return;
  }

  // 4. Phím Delete: Chuyển các mục đã chọn vào thùng rác
  if (e.key === 'Delete') {
    if (selectedPaths.size > 0) {
      e.preventDefault();
      bulkTrashBtn.click();
    }
  }
});

// =========================================================================
// BROWSER HISTORY POPSTATE (NÚT BACK TRÌNH DUYỆT & CỬ CHỈ BACK ĐIỆN THOẠI)
// =========================================================================
window.addEventListener('popstate', (e) => {
  // 1. Nếu có modal nào đang mở -> đóng modal trước mà không văng khỏi trang
  let modalClosed = false;
  if (!videoModal.hidden) { closeVideoPlayer(); modalClosed = true; }
  if (!audioModal.hidden) { closeAudioPlayer(); modalClosed = true; }
  if (!imageModal.hidden) { closeImageLightbox(); modalClosed = true; }
  if (!pdfModal.hidden) { closePdfViewer(); modalClosed = true; }
  if (!codeModal.hidden) { closeCodeViewer(); modalClosed = true; }
  if (!fileDetailsModal.hidden) { fileDetailsModal.hidden = true; modalClosed = true; }
  if (moveModal && !moveModal.hidden) { moveModal.hidden = true; modalClosed = true; }
  if (!newFolderModal.hidden) { newFolderModal.hidden = true; modalClosed = true; }
  if (!renameModal.hidden) { renameModal.hidden = true; modalClosed = true; }
  if (modalClosed) return;

  // 2. Nếu đang có tệp được chọn -> hủy chọn
  if (selectedPaths.size > 0) {
    selectedPaths.clear();
    document.querySelectorAll('.drive-card.is-selected').forEach(c => c.classList.remove('is-selected'));
    document.querySelectorAll('.card-checkbox:checked').forEach(cb => cb.checked = false);
    syncSelection();
    return;
  }

  // 3. Phục hồi thư mục trước đó trong lịch sử duyệt
  if (e.state && typeof e.state.folder === 'string') {
    state.currentFolder = e.state.folder;
    if (e.state.filter) state.currentFilter = e.state.filter;
    state.isTrash = !!e.state.isTrash;
    state.isVault = !!e.state.isVault;
    renderBreadcrumb();
    loadFiles();
  } else if (state.currentFolder) {
    state.currentFolder = '';
    renderBreadcrumb();
    loadFiles();
  }
});

// =========================================================================
// TOUCH SWIPE-BACK GESTURE (VUỐT MÀN HÌNH ĐỂ LÙI THƯ MỤC TRÊN WEB/MOBILE)
// =========================================================================
let touchStartX = 0;
let touchStartY = 0;
let isSwipingBack = false;

const swipeBackIndicator = document.createElement('div');
swipeBackIndicator.className = 'swipe-back-indicator';
swipeBackIndicator.innerHTML = `
  <div class="swipe-back-arrow">
    <svg viewBox="0 0 24 24"><path fill="#ffffff" d="M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20v-2z"/></svg>
  </div>
`;
document.body.appendChild(swipeBackIndicator);

window.addEventListener('touchstart', (e) => {
  if (e.touches.length !== 1) return;
  touchStartX = e.touches[0].clientX;
  touchStartY = e.touches[0].clientY;
  isSwipingBack = false;
}, { passive: true });

window.addEventListener('touchmove', (e) => {
  if (!touchStartX || e.touches.length !== 1) return;
  if (!state.currentFolder) return;

  const currentX = e.touches[0].clientX;
  const currentY = e.touches[0].clientY;
  const deltaX = currentX - touchStartX;
  const deltaY = Math.abs(currentY - touchStartY);

  // Vuốt ngang từ trái sang phải
  if (deltaX > 25 && deltaY < deltaX * 0.6) {
    isSwipingBack = true;
    const progress = Math.min(1, deltaX / 120);
    swipeBackIndicator.style.transform = `translateY(-50%) translateX(${Math.min(70, deltaX * 0.45) - 55}px) scale(${0.75 + progress * 0.25})`;
    swipeBackIndicator.style.opacity = `${progress}`;
    swipeBackIndicator.classList.toggle('is-ready', deltaX >= 90);
  } else if (!isSwipingBack) {
    swipeBackIndicator.style.opacity = '0';
  }
}, { passive: true });

window.addEventListener('touchend', (e) => {
  if (isSwipingBack) {
    const endX = e.changedTouches[0].clientX;
    const deltaX = endX - touchStartX;
    if (deltaX >= 90 && state.currentFolder) {
      if (navigator.vibrate) navigator.vibrate(25);
      navigateToParentFolder();
    }
    swipeBackIndicator.style.opacity = '0';
    swipeBackIndicator.style.transform = 'translateY(-50%) translateX(-55px)';
    swipeBackIndicator.classList.remove('is-ready');
  }
  touchStartX = 0;
  touchStartY = 0;
  isSwipingBack = false;
}, { passive: true });

// =========================================================================
// CONTEXT MENU
// =========================================================================
let activeContextMenu = null;

function showContextMenu(e, item) {
  closeContextMenu();
  const menu = document.createElement('div');
  menu.className = 'context-menu';

  if (item.isVault) {
    let vaultMenuHtml = '';

    // Nút xem trước cho ảnh/video vault
    if (item.type === 'video') {
      vaultMenuHtml += `
      <button type="button" class="context-item" data-action="preview">
        <svg viewBox="0 0 24 24"><path d="M8 5v14l11-7z"/></svg>
        <span>Phát video</span>
      </button>`;
    } else if (item.type === 'image') {
      vaultMenuHtml += `
      <button type="button" class="context-item" data-action="preview">
        <svg viewBox="0 0 24 24"><path d="M21 19V5c0-1.1-.9-2-2-2H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2zM8.5 13.5l2.5 3.01L14.5 12l4.5 6H5l3.5-4.5z"/></svg>
        <span>Xem ảnh</span>
      </button>`;
    }

    // Nút tải xuống chỉ cho file (không phải thư mục)
    if (!item.isFolder) {
      vaultMenuHtml += `
      <button type="button" class="context-item" data-action="download">
        <svg viewBox="0 0 24 24"><path d="M19 9h-4V3H9v6H5l7 7 7-7zM5 18v2h14v-2H5z"/></svg>
        <span>Tải xuống</span>
      </button>`;
    }

    vaultMenuHtml += `
      <button type="button" class="context-item" data-action="move">
        <svg viewBox="0 0 24 24"><path d="M10 4H4c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V8c0-1.1-.9-2-2-2h-8l-2-2zm8 9l-4-4v3H8v2h6v3l4-4z"/></svg>
        <span>Di chuyển...</span>
      </button>
      <button type="button" class="context-item" data-action="export-vault">
        <svg viewBox="0 0 24 24"><path d="M19 12v7H5v-7H3v7c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2v-7h-2zm-6 .67l2.59-2.58L17 11.5l-5 5-5-5 1.41-1.41L11 12.67V3h2v9.67z"/></svg>
        <span>Đưa về Drive</span>
      </button>
      <button type="button" class="context-item danger" data-action="delete">
        <svg viewBox="0 0 24 24"><path d="M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z"/></svg>
        <span>Xóa khỏi Thư mục bí mật</span>
      </button>`;

    menu.innerHTML = vaultMenuHtml;
    document.body.appendChild(menu);
    activeContextMenu = menu;
    const x = Math.min(e.clientX, window.innerWidth - 200);
    const y = Math.min(e.clientY, window.innerHeight - 150);
    menu.style.left = `${x}px`;
    menu.style.top = `${y}px`;
    menu.querySelectorAll('.context-item').forEach(btn => {
      btn.addEventListener('click', () => {
        const action = btn.dataset.action;
        closeContextMenu();
        handleContextAction(action, item);
      });
    });
    return;
  }

  if (!item.isFolder) {
    menu.innerHTML += `
      <button type="button" class="context-item" data-action="download">
        <svg viewBox="0 0 24 24"><path d="M19 9h-4V3H9v6H5l7 7 7-7zM5 18v2h14v-2H5z"/></svg>
        <span>Tải xuống</span>
      </button>
      <button type="button" class="context-item" data-action="copy-download">
        <svg viewBox="0 0 24 24"><path d="M16 1H4c-1.1 0-2 .9-2 2v14h2V3h12V1zm3 4H8c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h11c1.1 0 2-.9 2-2V7c0-1.1-.9-2-2-2zm0 16H8V7h11v14z"/></svg>
        <span>Sao chép link tải</span>
      </button>
    `;

    if (item.type === 'video' || item.type === 'audio') {
      menu.innerHTML += `
        <button type="button" class="context-item" data-action="copy-stream">
          <svg viewBox="0 0 24 24"><path d="M3.9 12c0-1.71 1.39-3.1 3.1-3.1h4V7H7c-2.76 0-5 2.24-5 5s2.24 5 5 5h4v-1.9H7c-1.71 0-3.1-1.39-3.1-3.1zM8 13h8v-2H8v2zm9-6h-4v1.9h4c1.71 0 3.1 1.39 3.1 3.1s-1.39 3.1-3.1 3.1h-4V17h4c2.76 0 5-2.24 5-5s-2.24-5-5-5z"/></svg>
          <span>Sao chép link stream</span>
        </button>
      `;
    }
  }

  if (state.isTrash) {
    menu.innerHTML += `
      <button type="button" class="context-item" data-action="restore">
        <svg viewBox="0 0 24 24"><path d="M13 3a9 9 0 0 0-9 9H1l3.89 3.89.07.14L9 12H6c0-3.87 3.13-7 7-7s7 3.13 7 7-3.13 7-7 7c-1.93 0-3.68-.79-4.94-2.06l-1.42 1.42A8.954 8.954 0 0 0 13 21a9 9 0 0 0 0-18zm-1 5v5l4.28 2.54.72-1.21-3.5-2.08V8H12z"/></svg>
        <span>Khôi phục</span>
      </button>
      <button type="button" class="context-item danger" data-action="delete">
        <svg viewBox="0 0 24 24"><path d="M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z"/></svg>
        <span>Xóa vĩnh viễn</span>
      </button>
    `;
  } else {
    menu.innerHTML += `
      <button type="button" class="context-item" data-action="rename">
        <svg viewBox="0 0 24 24"><path d="M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zM20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34c-.39-.39-1.02-.39-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z"/></svg>
        <span>Đổi tên</span>
      </button>
      <button type="button" class="context-item" data-action="move">
        <svg viewBox="0 0 24 24"><path d="M10 4H4c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V8c0-1.1-.9-2-2-2h-8l-2-2zm8 9l-4-4v3H8v2h6v3l4-4z"/></svg>
        <span>Di chuyển tới...</span>
      </button>
    `;

    menu.innerHTML += `
      <button type="button" class="context-item" data-action="import-vault" style="color:#f43f5e;">
        <svg viewBox="0 0 24 24"><path fill="#f43f5e" d="M18 8h-1V6c0-2.76-2.24-5-5-5S7 3.24 7 6v2H6c-1.1 0-2 .9-2 2v10c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V10c0-1.1-.9-2-2-2zm-6 9c-1.1 0-2-.9-2-2s.9-2 2-2 2 .9 2 2-.9 2-2 2zm3.1-9H8.9V6c0-1.71 1.39-3.1 3.1-3.1 1.71 0 3.1 1.39 3.1 3.1v2z"/></svg>
        <span>Chuyển sang Thư mục bí mật</span>
      </button>
    `;

    menu.innerHTML += `
      <button type="button" class="context-item" data-action="details">
        <svg viewBox="0 0 24 24"><path d="M11 7h2v2h-2zm0 4h2v6h-2zm1-9C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8z"/></svg>
        <span>Chi tiết tệp</span>
      </button>
    `;

    menu.innerHTML += `
      <button type="button" class="context-item danger" data-action="delete">
        <svg viewBox="0 0 24 24"><path d="M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z"/></svg>
        <span>Chuyển vào Thùng rác</span>
      </button>
    `;
  }

  document.body.appendChild(menu);
  activeContextMenu = menu;

  const x = Math.min(e.clientX, window.innerWidth - 200);
  const y = Math.min(e.clientY, window.innerHeight - 150);
  menu.style.left = `${x}px`;
  menu.style.top = `${y}px`;

  menu.querySelectorAll('.context-item').forEach(btn => {
    btn.addEventListener('click', () => {
      const action = btn.dataset.action;
      closeContextMenu();
      handleContextAction(action, item);
    });
  });
}

function closeContextMenu() {
  if (activeContextMenu) {
    activeContextMenu.remove();
    activeContextMenu = null;
  }
}

document.addEventListener('click', closeContextMenu);

async function handleContextAction(action, item) {
  if (action === 'move') {
    openMoveModal([item.path]);
  } else if (action === 'preview') {
    openItem(item);
  } else if (action === 'copy-download') {
    const token = state.token || '';
    const tokenParam = token ? `&token=${encodeURIComponent(token)}` : '';
    const origin = window.location.origin;
    const url = item.isVault
      ? `${origin}${api(`/api/vault/download?id=${encodeURIComponent(item.id)}`)}${tokenParam}`
      : `${origin}${api(`/api/download?path=${encodeURIComponent(item.path)}`)}${tokenParam}`;
    navigator.clipboard.writeText(url).then(() => {
      showToast('Đã sao chép liên kết tải xuống!');
    }).catch(() => {
      prompt('Liên kết tải xuống:', url);
    });
  } else if (action === 'copy-stream') {
    const token = state.token || '';
    const tokenParam = token ? `&token=${encodeURIComponent(token)}` : '';
    const origin = window.location.origin;
    const url = item.isVault
      ? `${origin}${api(`/api/vault/stream?id=${encodeURIComponent(item.id)}`)}${tokenParam}`
      : `${origin}${api(`/api/stream?path=${encodeURIComponent(item.path)}`)}${tokenParam}`;
    navigator.clipboard.writeText(url).then(() => {
      showToast('Đã sao chép liên kết phát trực tiếp (Stream URL)!');
    }).catch(() => {
      prompt('Liên kết Stream:', url);
    });
  } else if (action === 'details') {
    openFileDetails(item);
  } else if (action === 'download') {
    if (item.isVault) {
      window.open(api(`/api/vault/download?id=${encodeURIComponent(item.id)}`), '_blank');
    } else {
      window.open(api(`/api/download?path=${encodeURIComponent(item.path)}`), '_blank');
    }
  } else if (action === 'import-vault') {
    await moveToVault([item.path]);
  } else if (action === 'export-vault') {
    try {
      const res = await authFetch(api('/api/vault/export'), {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ id: item.id, targetFolder: '' })
      });
      const data = await res.json();
      if (data.ok) {
        alert('Đã đưa mục về lại Drive thành công!');
        loadFiles();
      } else {
        alert(data.error || 'Lỗi xuất khỏi Thư mục bí mật');
      }
    } catch (err) {
      alert('Lỗi: ' + err.message);
    }
  } else if (action === 'rename') {
    renameTargetItem = item;
    renameInput.value = item.name;
    renameModal.hidden = false;
    renameInput.focus();
    renameInput.select();
  } else if (action === 'delete') {
    if (item.isVault) {
      if (!confirm(`Bạn có chắc muốn xóa vĩnh viễn "${item.name}" khỏi Thư mục bí mật?`)) return;
      try {
        const res = await authFetch(api('/api/vault/delete'), {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ id: item.id })
        });
        const data = await res.json();
        if (data.ok) {
          loadFiles();
        } else {
          alert(data.error || 'Lỗi xóa khỏi Thư mục bí mật');
        }
      } catch (err) {
        alert('Lỗi: ' + err.message);
      }
      return;
    }

    const msg = state.isTrash
      ? `Bạn có chắc muốn xóa vĩnh viễn "${item.name}"? Thao tác này không thể hoàn tác.`
      : `Chuyển "${item.name}" vào Thùng rác?`;
    if (!confirm(msg)) return;

    try {
      const res = await authFetch(api('/api/delete'), {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ paths: [item.path] })
      });
      const data = await res.json();
      if (data.ok) {
        selectedPaths.delete(item.path);
        loadFiles();
        fetchStorage();
      } else {
        alert(data.error || 'Lỗi xóa tệp');
      }
    } catch (err) {
      alert('Lỗi kết nối: ' + err.message);
    }
  } else if (action === 'restore') {
    try {
      const res = await authFetch(api('/api/restore'), {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ paths: [item.path] })
      });
      const data = await res.json();
      if (data.ok) {
        selectedPaths.delete(item.path);
        loadFiles();
        fetchStorage();
      } else {
        alert(data.error || 'Lỗi khôi phục');
      }
    } catch (err) {
      alert('Lỗi kết nối: ' + err.message);
    }
  }
}

// Rename Confirmation
confirmRenameBtn.addEventListener('click', async () => {
  const newName = renameInput.value.trim();
  if (!newName || !renameTargetItem) return;

  try {
    const res = await authFetch(api('/api/rename'), {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ oldPath: renameTargetItem.path, newName })
    });
    const data = await res.json();
    if (data.ok) {
      renameModal.hidden = true;
      loadFiles();
    } else {
      alert(data.error || 'Lỗi đổi tên');
    }
  } catch (err) {
    alert('Lỗi kết nối: ' + err.message);
  }
});
cancelRenameBtn.addEventListener('click', () => { renameModal.hidden = true; });

// New Folder Modal
function openNewFolderModal() {
  newFolderNameInput.value = 'Thư mục không có tiêu đề';
  newFolderModal.hidden = false;
  newFolderNameInput.focus();
  newFolderNameInput.select();
}

toolbarNewFolderBtn.addEventListener('click', openNewFolderModal);
menuNewFolder.addEventListener('click', () => {
  newDropdown.hidden = true;
  openNewFolderModal();
});

cancelNewFolderBtn.addEventListener('click', () => { newFolderModal.hidden = true; });
confirmNewFolderBtn.addEventListener('click', async () => {
  const name = newFolderNameInput.value.trim();
  if (!name) return;

  try {
    const res = await authFetch(api('/api/folder'), {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ folder: state.currentFolder, name })
    });
    const data = await res.json();
    if (data.ok) {
      newFolderModal.hidden = true;
      loadFiles();
    } else {
      alert(data.error || 'Lỗi tạo thư mục');
    }
  } catch (err) {
    alert('Lỗi kết nối: ' + err.message);
  }
});

// =========================================================================
// MOVE FILES & FOLDERS PIPELINE (Di chuyển tệp / thư mục)
// =========================================================================
let pendingMovePaths = [];
let movePickerCurrentFolder = '';

async function executeMove(paths, targetFolder) {
  if (!paths || paths.length === 0) return;
  isBusy = true;
  if (stageLoadingBar) stageLoadingBar.hidden = false;
  try {
    const res = await authFetch(api('/api/move'), {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        paths,
        targetFolder: targetFolder || '',
        isVault: state.isVault
      })
    });
    const data = await res.json();
    if (data.ok) {
      const destName = targetFolder ? targetFolder.split('/').pop() : (state.isVault ? 'Thư mục bí mật' : 'Drive của tôi');
      showToast(`✓ Đã di chuyển ${data.movedCount || paths.length} mục vào "${destName}"`, 'success');
      selectedPaths.clear();
      syncSelection();
      loadFiles();
      fetchStorage();
    } else {
      showToast(data.error || 'Lỗi di chuyển tệp', 'error');
    }
  } catch (err) {
    showToast('Lỗi: ' + err.message, 'error');
  } finally {
    isBusy = false;
    if (stageLoadingBar) stageLoadingBar.hidden = true;
  }
}

async function openMoveModal(paths = []) {
  if (!paths || paths.length === 0) return;
  pendingMovePaths = paths;
  movePickerCurrentFolder = state.currentFolder || '';

  const count = paths.length;
  const nameDesc = count === 1 ? `"${paths[0].split('/').pop()}"` : `${count} mục đã chọn`;
  if (moveModalTitle) moveModalTitle.textContent = `Di chuyển ${nameDesc}`;
  if (moveModalSubTitle) moveModalSubTitle.textContent = 'Chọn thư mục đích rồi nhấn "Di chuyển vào đây"';

  if (moveModal) moveModal.hidden = false;
  await renderMovePicker();
}

async function renderMovePicker() {
  if (!movePickerBreadcrumb || !movePickerList) return;

  // 1. Render Breadcrumb trong Modal
  movePickerBreadcrumb.innerHTML = '';
  const rootBtn = document.createElement('button');
  rootBtn.type = 'button';
  rootBtn.className = 'move-crumb-btn' + (!movePickerCurrentFolder ? ' active' : '');
  rootBtn.textContent = state.isVault ? 'Thư mục bí mật' : 'Drive của tôi';
  rootBtn.addEventListener('click', () => {
    if (movePickerCurrentFolder !== '') {
      movePickerCurrentFolder = '';
      renderMovePicker();
    }
  });
  movePickerBreadcrumb.appendChild(rootBtn);

  if (movePickerCurrentFolder) {
    const parts = movePickerCurrentFolder.split('/').filter(Boolean);
    let accum = '';
    parts.forEach((p, idx) => {
      accum = accum ? `${accum}/${p}` : p;
      const pathRef = accum;
      const isLast = idx === parts.length - 1;

      const sep = document.createElement('span');
      sep.className = 'move-crumb-sep';
      sep.textContent = '›';
      movePickerBreadcrumb.appendChild(sep);

      const btn = document.createElement('button');
      btn.type = 'button';
      btn.className = 'move-crumb-btn' + (isLast ? ' active' : '');
      btn.textContent = p;
      if (!isLast) {
        btn.addEventListener('click', () => {
          movePickerCurrentFolder = pathRef;
          renderMovePicker();
        });
      }
      movePickerBreadcrumb.appendChild(btn);
    });
  }

  // Cập nhật trạng thái nút xác nhận: Vô hiệu nếu đang ở chính thư mục cha của các tệp cần chuyển
  if (confirmMoveBtn) {
    const isSameAsCurrent = pendingMovePaths.every(p => {
      const parent = p.includes('/') ? p.substring(0, p.lastIndexOf('/')) : '';
      return parent === movePickerCurrentFolder;
    });
    const isInsideSelf = pendingMovePaths.some(p => {
      return movePickerCurrentFolder === p || movePickerCurrentFolder.startsWith(p + '/');
    });

    confirmMoveBtn.disabled = isSameAsCurrent || isInsideSelf;
    if (isSameAsCurrent) {
      confirmMoveBtn.title = 'Mục đã ở trong thư mục này rồi';
    } else if (isInsideSelf) {
      confirmMoveBtn.title = 'Không thể di chuyển thư mục vào bên trong chính nó';
    } else {
      confirmMoveBtn.title = 'Di chuyển các mục vào thư mục này';
    }
  }

  // 2. Tải danh sách thư mục con
  movePickerList.innerHTML = '<div style="text-align:center;padding:24px;color:var(--text-muted);">Đang tải danh sách thư mục...</div>';

  try {
    const res = await authFetch(api(`/api/folders?folder=${encodeURIComponent(movePickerCurrentFolder)}&vault=${state.isVault ? '1' : '0'}`));
    const data = await res.json();
    if (!data.ok) throw new Error(data.error || 'Lỗi tải thư mục');

    movePickerList.innerHTML = '';
    const folders = data.folders || [];

    // Lọc các thư mục không được phép di chuyển vào (chính nó)
    const validFolders = folders.filter(f => !pendingMovePaths.includes(f.path));

    if (validFolders.length === 0) {
      movePickerList.innerHTML = `
        <div class="move-picker-empty">
          <svg style="width:36px;height:36px;fill:var(--text-muted);opacity:0.6;margin-bottom:8px;" viewBox="0 0 24 24"><path d="M10 4H4c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V8c0-1.1-.9-2-2-2h-8l-2-2z"/></svg>
          <div>Không có thư mục con nào ở đây</div>
          <div style="font-size:11.5px;color:var(--text-muted);margin-top:4px;">Bạn có thể nhấn "Di chuyển vào đây" hoặc tạo "Thư mục mới"</div>
        </div>
      `;
      return;
    }

    for (const f of validFolders) {
      const itemEl = document.createElement('div');
      itemEl.className = 'move-picker-item';
      itemEl.innerHTML = `
        <div class="move-picker-item-icon">
          <svg viewBox="0 0 24 24"><path fill="#f59e0b" d="M10 4H4c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V8c0-1.1-.9-2-2-2h-8l-2-2z"/><path fill="#fde68a" opacity="0.3" d="M2 18V9c0-1.1.9-2 2-2h6l2 2h8c1.1 0 2 .9 2 2v7c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2z"/></svg>
        </div>
        <span class="move-picker-item-name" title="${escapeHtml(f.name)}">${escapeHtml(f.name)}</span>
        <span class="move-picker-item-arrow">›</span>
      `;
      itemEl.addEventListener('click', () => {
        movePickerCurrentFolder = f.path;
        renderMovePicker();
      });
      movePickerList.appendChild(itemEl);
    }
  } catch (err) {
    movePickerList.innerHTML = `<div style="text-align:center;padding:20px;color:#f43f5e;">Lỗi: ${escapeHtml(err.message)}</div>`;
  }
}

if (cancelMoveBtn) {
  cancelMoveBtn.addEventListener('click', () => {
    if (moveModal) moveModal.hidden = true;
    pendingMovePaths = [];
  });
}

if (confirmMoveBtn) {
  confirmMoveBtn.addEventListener('click', async () => {
    if (pendingMovePaths.length === 0 || confirmMoveBtn.disabled) return;
    const paths = [...pendingMovePaths];
    const target = movePickerCurrentFolder;
    if (moveModal) moveModal.hidden = true;
    pendingMovePaths = [];
    await executeMove(paths, target);
  });
}

if (moveNewFolderBtn) {
  moveNewFolderBtn.addEventListener('click', async () => {
    const name = prompt('Nhập tên thư mục mới muốn tạo:');
    if (!name || !name.trim()) return;
    const cleanName = name.trim();
    try {
      const res = await authFetch(api('/api/folder'), {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          folder: movePickerCurrentFolder,
          name: cleanName
        })
      });
      const data = await res.json();
      if (data.ok) {
        showToast(`Đã tạo thư mục "${cleanName}"`, 'success');
        movePickerCurrentFolder = movePickerCurrentFolder ? `${movePickerCurrentFolder}/${cleanName}` : cleanName;
        renderMovePicker();
      } else {
        alert(data.error || 'Lỗi tạo thư mục');
      }
    } catch (e) {
      alert('Lỗi: ' + e.message);
    }
  });
}

if (bulkMoveBtn) {
  bulkMoveBtn.addEventListener('click', () => {
    if (selectedPaths.size === 0 || isBusy) return;
    openMoveModal(Array.from(selectedPaths));
  });
}

// =========================================================================
// ULTRA HIGH-SPEED ADAPTIVE UPLOAD PIPELINE
// - Mạng LAN: Upload Stream trực tiếp tốc độ Gigabit (50-100+ MB/s, 0 re-merge)
// - Qua Cloudflare: Direct Stream với file <= 75MB, Chunked 25MB với file lớn
// =========================================================================
function isDirectLanConnection() {
  const h = window.location.hostname;
  return (
    h.startsWith('192.168.') ||
    h.endsWith('.lan') ||
    h === 'localhost' ||
    h === '127.0.0.1' ||
    h.startsWith('10.') ||
    h.startsWith('172.') ||
    window.location.port === '8080'
  );
}

function formatSpeed(bytesPerSec) {
  if (bytesPerSec <= 0 || !isFinite(bytesPerSec)) return '';
  if (bytesPerSec >= 1024 * 1024) {
    return `${(bytesPerSec / (1024 * 1024)).toFixed(1)} MB/s`;
  }
  return `${(bytesPerSec / 1024).toFixed(0)} KB/s`;
}

const uploadQueue = [];
let activeUploadCount = 0;
let refreshDebounceTimer = null;

function triggerRefreshDebounced() {
  clearTimeout(refreshDebounceTimer);
  refreshDebounceTimer = setTimeout(() => {
    loadFiles();
    fetchStorage();
  }, 400);
}

// Trích xuất file đệ quy từ Drag & Drop (Folder và Folder lồng nhau)
async function extractFilesFromDataTransfer(dataTransfer) {
  const result = [];

  async function traverseEntry(entry, currentPath = '') {
    if (entry.isFile) {
      await new Promise((resolve) => {
        entry.file((file) => {
          result.push({
            file,
            relPath: currentPath ? `${currentPath}/${file.name}` : file.name
          });
          resolve();
        }, () => resolve());
      });
    } else if (entry.isDirectory) {
      const dirReader = entry.createReader();
      const nextPath = currentPath ? `${currentPath}/${entry.name}` : entry.name;

      const readAllEntries = async () => {
        return new Promise((resolve) => {
          dirReader.readEntries(async (entries) => {
            if (!entries || entries.length === 0) {
              resolve();
            } else {
              for (const child of entries) {
                await traverseEntry(child, nextPath);
              }
              await readAllEntries();
              resolve();
            }
          }, () => resolve());
        });
      };
      await readAllEntries();
    }
  }

  if (dataTransfer.items && dataTransfer.items.length > 0) {
    const entries = [];
    for (let i = 0; i < dataTransfer.items.length; i++) {
      const item = dataTransfer.items[i];
      if (typeof item.webkitGetAsEntry === 'function') {
        const entry = item.webkitGetAsEntry();
        if (entry) entries.push(entry);
      } else if (item.kind === 'file') {
        const file = item.getAsFile();
        if (file) result.push({ file, relPath: file.name });
      }
    }
    for (const entry of entries) {
      await traverseEntry(entry, '');
    }
  } else if (dataTransfer.files) {
    for (const file of dataTransfer.files) {
      result.push({
        file,
        relPath: file.webkitRelativePath || file.name
      });
    }
  }

  return result;
}

function queueUploads(items) {
  if (!items || items.length === 0) return;
  uploadDrawer.hidden = false;
  uploadDrawer.classList.remove('minimized');

  const normalized = items.map(item => {
    if (item instanceof File) {
      return {
        file: item,
        relPath: item.webkitRelativePath || item.name
      };
    }
    return item;
  });

  const total = uploadQueue.length + normalized.length;
  drawerTitle.textContent = `Đang tải lên ${total} mục...`;

  for (const { file, relPath } of normalized) {
    const displayName = relPath || file.name;
    const row = document.createElement('div');
    row.className = 'upload-item-row';
    row.innerHTML = `
      <div class="upload-item-header">
        <span class="upload-item-name" title="${escapeHtml(displayName)}">${escapeHtml(displayName)}</span>
        <span class="upload-item-status">Chờ...</span>
      </div>
      <div class="upload-item-progress">
        <div class="upload-item-fill"></div>
      </div>
    `;
    drawerItemsList.prepend(row);

    uploadQueue.push({
      file,
      relPath: displayName,
      origName: displayName,
      folder: state.currentFolder,
      isVault: state.isVault,
      uiName: row.querySelector('.upload-item-name'),
      uiStatus: row.querySelector('.upload-item-status'),
      uiFill: row.querySelector('.upload-item-fill')
    });
  }

  processUploadQueue();
}

async function uploadItem(item) {
  const { file, relPath, folder, uiStatus, uiFill, isVault } = item;
  uiStatus.textContent = 'Bắt đầu...';
  const totalSize = file.size;

  if (isVault || state.isVault) {
    let lastLoaded = 0;
    let lastTime = Date.now();
    await new Promise((resolve, reject) => {
      const xhr = new XMLHttpRequest();
      const url = api(`/api/vault/upload?name=${encodeURIComponent(item.origName || file.name)}`);
      xhr.open('POST', url, true);
      xhr.withCredentials = true;
      const token = getAuthToken();
      if (token) xhr.setRequestHeader('Authorization', `Bearer ${token}`);

      xhr.upload.onprogress = (e) => {
        if (e.lengthComputable) {
          const pct = Math.min(99, Math.round((e.loaded / totalSize) * 100));
          uiFill.style.width = `${pct}%`;
          const now = Date.now();
          const timeDiff = (now - lastTime) / 1000;
          if (timeDiff >= 0.3) {
            const speed = (e.loaded - lastLoaded) / timeDiff;
            const speedText = formatSpeed(speed);
            uiStatus.textContent = speedText ? `${speedText} • ${pct}%` : `${pct}%`;
            lastLoaded = e.loaded;
            lastTime = now;
          }
        }
      };

      xhr.onload = () => {
        if (xhr.status >= 200 && xhr.status < 300) {
          uiFill.style.width = '100%';
          uiStatus.textContent = 'Đã mã hóa an toàn!';
          uiStatus.style.color = '#10b981';
          resolve();
          loadFiles();
          fetchStorage();
        } else {
          try {
            const resp = JSON.parse(xhr.responseText || '{}');
            reject(new Error(resp.error || `HTTP ${xhr.status}`));
          } catch (_) {
            reject(new Error(`HTTP ${xhr.status}`));
          }
        }
      };
      xhr.onerror = () => reject(new Error('Lỗi kết nối'));
      xhr.send(file);
    });
    return;
  }

  const isLan = isDirectLanConnection();
  // Trong LAN: Stream trực tiếp 100% dung lượng, không chia nhỏ, tận dụng tối đa băng thông Gigabit LAN
  // Qua Cloudflare: Stream trực tiếp nếu <= 75MB, hoặc chia khối 25MB nếu > 75MB
  const useStream = isLan || totalSize <= 75 * 1024 * 1024;

  if (useStream) {
    let lastLoaded = 0;
    let lastTime = Date.now();

    await new Promise((resolve, reject) => {
      const xhr = new XMLHttpRequest();
      const url = api(`/api/upload-stream?folder=${encodeURIComponent(folder)}&relPath=${encodeURIComponent(relPath)}`);
      xhr.open('POST', url, true);
      xhr.withCredentials = true;
      const token = getAuthToken();
      if (token) {
        xhr.setRequestHeader('Authorization', `Bearer ${token}`);
      }

      xhr.upload.onprogress = (e) => {
        if (e.lengthComputable) {
          const pct = Math.min(99, Math.round((e.loaded / totalSize) * 100));
          uiFill.style.width = `${pct}%`;
          const now = Date.now();
          const timeDiff = (now - lastTime) / 1000;
          if (timeDiff >= 0.3) {
            const speed = (e.loaded - lastLoaded) / timeDiff;
            const speedText = formatSpeed(speed);
            uiStatus.textContent = speedText ? `${speedText} • ${pct}%` : `${pct}%`;
            lastLoaded = e.loaded;
            lastTime = now;
          }
        }
      };

      xhr.onload = () => {
        if (xhr.status >= 200 && xhr.status < 300) {
          try {
            const resp = JSON.parse(xhr.responseText || '{}');
            if (resp.name && resp.name !== item.origName) {
              item.uploadedName = resp.name;
            }
          } catch (_) {}
          resolve();
          // Cập nhật giao diện tức thì ngay khi file vừa xong
          loadFiles();
          fetchStorage();
        } else {
          reject(new Error(`HTTP ${xhr.status}`));
        }
      };

      xhr.onerror = () => reject(new Error('Lỗi mạng'));
      xhr.ontimeout = () => reject(new Error('Hết thời gian'));
      xhr.send(file);
    });
  } else {
    // CHUNKED UPLOAD CHO FILE LỚN QUA CLOUDFLARE TUNNEL (25MB mỗi mảnh)
    const CHUNK_SIZE = 25 * 1024 * 1024; // 25MB slices
    const totalChunks = Math.max(1, Math.ceil(totalSize / CHUNK_SIZE));
    const uploadId = 'up_' + Date.now() + '_' + Math.random().toString(36).slice(2, 8);

    let lastLoaded = 0;
    let lastTime = Date.now();

    for (let chunkIndex = 0; chunkIndex < totalChunks; chunkIndex++) {
      const start = chunkIndex * CHUNK_SIZE;
      const end = Math.min(totalSize, start + CHUNK_SIZE);
      const chunkBlob = file.slice(start, end);

      let chunkSuccess = false;
      let retries = 0;

      while (!chunkSuccess && retries < 3) {
        try {
          await new Promise((resolve, reject) => {
            const xhr = new XMLHttpRequest();
            const url = api(`/api/upload-chunk?folder=${encodeURIComponent(folder)}&relPath=${encodeURIComponent(relPath)}&uploadId=${encodeURIComponent(uploadId)}&chunkIndex=${chunkIndex}&totalChunks=${totalChunks}&totalSize=${totalSize}`);
            xhr.open('POST', url, true);
            xhr.withCredentials = true;
            const token = getAuthToken();
            if (token) {
              xhr.setRequestHeader('Authorization', `Bearer ${token}`);
            }

            xhr.upload.onprogress = (e) => {
              if (e.lengthComputable) {
                const currentChunkBytes = e.loaded;
                const totalLoaded = start + currentChunkBytes;
                const pct = Math.min(99, Math.round((totalLoaded / totalSize) * 100));
                uiFill.style.width = `${pct}%`;

                const now = Date.now();
                const timeDiff = (now - lastTime) / 1000;
                if (timeDiff >= 0.3) {
                  const speed = (totalLoaded - lastLoaded) / timeDiff;
                  const speedText = formatSpeed(speed);
                  uiStatus.textContent = speedText ? `${speedText} • ${pct}%` : `${pct}%`;
                  lastLoaded = totalLoaded;
                  lastTime = now;
                }
              }
            };

            xhr.onload = () => {
              if (xhr.status >= 200 && xhr.status < 300) {
                try {
                  const resp = JSON.parse(xhr.responseText || '{}');
                  if (resp.name && resp.name !== item.origName) {
                    item.uploadedName = resp.name;
                  }
                } catch (_) {}
                resolve();
              } else {
                reject(new Error(`HTTP ${xhr.status}`));
              }
            };

            xhr.onerror = () => reject(new Error('Lỗi mạng'));
            xhr.ontimeout = () => reject(new Error('Hết thời gian'));
            xhr.send(chunkBlob);
          });
          chunkSuccess = true;
        } catch (err) {
          retries++;
          if (retries >= 3) throw err;
          await new Promise(r => setTimeout(r, 1000));
        }
      }
    }
  }

  uiFill.style.width = '100%';
  uiFill.classList.add('done');
  if (item.uploadedName && item.uploadedName !== item.origName) {
    uiStatus.innerHTML = `<span style="color:#059669;font-weight:600;" title="Đã tự động đổi tên do trùng lặp: ${escapeHtml(item.uploadedName)}">✓ Đã đổi tên: ${escapeHtml(item.uploadedName)}</span>`;
    if (item.uiName) {
      item.uiName.textContent = item.uploadedName;
      item.uiName.title = `Đã tự động đổi tên từ "${item.origName}" thành "${item.uploadedName}" do trùng lặp tệp`;
    }
  } else {
    uiStatus.textContent = '✓ Hoàn tất';
  }
  loadFiles();
  fetchStorage();
}

function processUploadQueue() {
  const isLan = isDirectLanConnection();
  const maxConcurrent = isLan ? 3 : 2;

  if (uploadQueue.length === 0 && activeUploadCount === 0) {
    drawerTitle.textContent = 'Tải lên hoàn tất';
    loadFiles();
    fetchStorage();
    return;
  }

  while (activeUploadCount < maxConcurrent && uploadQueue.length > 0) {
    const item = uploadQueue.shift();
    activeUploadCount++;
    drawerTitle.textContent = `Đang tải lên (${uploadQueue.length + activeUploadCount} mục còn lại)...`;

    uploadItem(item)
      .catch((err) => {
        console.error('Lỗi tải tệp:', err);
        item.uiStatus.textContent = '✕ Lỗi';
        item.uiFill.style.background = '#ea4335';
      })
      .finally(() => {
        activeUploadCount--;
        processUploadQueue();
        triggerRefreshDebounced();
      });
  }
}

// Upload Trigger Buttons
menuUploadFile.addEventListener('click', () => {
  newDropdown.hidden = true;
  hiddenFileInput.click();
});
toolbarUploadBtn.addEventListener('click', () => hiddenFileInput.click());
menuUploadFolder.addEventListener('click', () => {
  newDropdown.hidden = true;
  hiddenFolderInput.click();
});

hiddenFileInput.addEventListener('change', (e) => {
  if (e.target.files && e.target.files.length > 0) {
    const list = Array.from(e.target.files).map(f => ({
      file: f,
      relPath: f.name
    }));
    queueUploads(list);
  }
  hiddenFileInput.value = '';
});

hiddenFolderInput.addEventListener('change', (e) => {
  if (e.target.files && e.target.files.length > 0) {
    const list = Array.from(e.target.files).map(f => ({
      file: f,
      relPath: f.webkitRelativePath || f.name
    }));
    queueUploads(list);
  }
  hiddenFolderInput.value = '';
});

minimizeDrawerBtn.addEventListener('click', () => {
  uploadDrawer.classList.toggle('minimized');
});

closeDrawerBtn.addEventListener('click', () => {
  uploadDrawer.hidden = true;
});

// Drag & Drop
window.addEventListener('dragenter', (e) => {
  e.preventDefault();
  dragDropOverlay.hidden = false;
});
dragDropOverlay.addEventListener('dragover', (e) => {
  e.preventDefault();
});
dragDropOverlay.addEventListener('dragleave', (e) => {
  if (e.relatedTarget === null) {
    dragDropOverlay.hidden = true;
  }
});
dragDropOverlay.addEventListener('drop', async (e) => {
  e.preventDefault();
  dragDropOverlay.hidden = true;
  if (e.dataTransfer) {
    const items = await extractFilesFromDataTransfer(e.dataTransfer);
    if (items.length > 0) {
      queueUploads(items);
    }
  }
});

// =========================================================================
// SEARCH & FILTERS
// =========================================================================
let searchDebounce = null;
searchInput.addEventListener('input', () => {
  const q = searchInput.value.trim();
  searchClearBtn.hidden = !q;
  clearTimeout(searchDebounce);
  searchDebounce = setTimeout(() => {
    state.searchQuery = q;
    selectedPaths.clear();
    loadFiles();
  }, 250);
});

searchClearBtn.addEventListener('click', () => {
  searchInput.value = '';
  searchClearBtn.hidden = true;
  state.searchQuery = '';
  selectedPaths.clear();
  loadFiles();
});

// Sidebar Navigation
document.querySelectorAll('.sidebar-nav .nav-link').forEach(link => {
  link.addEventListener('click', (e) => {
    e.preventDefault();
    document.querySelectorAll('.sidebar-nav .nav-link').forEach(l => l.classList.remove('active'));
    link.classList.add('active');

    state.currentFilter = link.dataset.filter || 'all';
    state.isTrash = link.dataset.trash === '1';
    state.isVault = link.dataset.vault === '1';
    state.searchQuery = '';
    searchInput.value = '';
    searchClearBtn.hidden = true;
    state.currentFolder = '';
    selectedPaths.clear();

    if (window.innerWidth <= 768) {
      driveSidebar.classList.remove('open');
    }
    loadFiles();
  });
});

// FAB "+ Mới" Dropdown
newFabBtn.addEventListener('click', (e) => {
  e.stopPropagation();
  newDropdown.hidden = !newDropdown.hidden;
});
document.addEventListener('click', () => {
  newDropdown.hidden = true;
});

// Grid / List View Toggle
viewToggleBtn.addEventListener('click', () => {
  state.isGridView = !state.isGridView;
  fileViewContainer.className = state.isGridView
    ? 'file-view-container grid-mode'
    : 'file-view-container list-mode';
  viewGridIcon.hidden = !state.isGridView;
  viewListIcon.hidden = state.isGridView;
});

// Refresh
refreshBtn.addEventListener('click', () => {
  loadFiles();
  fetchStorage();
});

// Mobile Sidebar & Backdrop Overlay
const sidebarBackdrop = document.querySelector('#sidebarBackdrop');
if (mobileMenuBtn && driveSidebar) {
  mobileMenuBtn.addEventListener('click', () => {
    const isOpen = driveSidebar.classList.toggle('open');
    if (sidebarBackdrop) sidebarBackdrop.hidden = !isOpen;
  });
}
if (sidebarBackdrop && driveSidebar) {
  sidebarBackdrop.addEventListener('click', () => {
    driveSidebar.classList.remove('open');
    sidebarBackdrop.hidden = true;
  });
}

// Global Keyboard Shortcuts (Ctrl+K / ⌘K to search)
window.addEventListener('keydown', (e) => {
  if ((e.ctrlKey || e.metaKey) && e.key.toLowerCase() === 'k') {
    e.preventDefault();
    if (searchInput) {
      searchInput.focus();
      searchInput.select();
    }
  }
});

// =========================================================================
// THEME SWITCHER (Dark / Light Mode)
// =========================================================================
function initTheme() {
  const themeToggleBtn = document.querySelector('#themeToggleBtn');
  const sunIcon = document.querySelector('#themeSunIcon');
  const moonIcon = document.querySelector('#themeMoonIcon');

  function apply(theme) {
    document.documentElement.setAttribute('data-theme', theme);
    if (sunIcon && moonIcon) {
      if (theme === 'dark') {
        sunIcon.hidden = false;
        moonIcon.hidden = true;
      } else {
        sunIcon.hidden = true;
        moonIcon.hidden = false;
      }
    }
  }

  const savedTheme = localStorage.getItem('nas_theme') || document.documentElement.getAttribute('data-theme') || 'dark';
  apply(savedTheme);

  if (themeToggleBtn) {
    themeToggleBtn.addEventListener('click', () => {
      const active = document.documentElement.getAttribute('data-theme') === 'dark' ? 'light' : 'dark';
      apply(active);
      localStorage.setItem('nas_theme', active);
    });
  }
}

// =========================================================================
// NETWORK ENVIRONMENT DETECTION (LAN vs CLOUDFLARE TUNNEL)
// =========================================================================
function initNetworkDetection() {
  const netBadge = document.querySelector('#netStatusBadge');
  const netText = document.querySelector('#netStatusText');
  const lanBanner = document.querySelector('#lanBanner');
  const dismissBtn = document.querySelector('#dismissLanBanner');

  if (!netBadge || !netText) return;

  const hostname = window.location.hostname;
  const isLocalHost = hostname === '192.168.88.230' ||
                      hostname === 'localhost' ||
                      hostname === '127.0.0.1' ||
                      hostname.startsWith('192.168.') ||
                      hostname.startsWith('10.') ||
                      hostname.startsWith('172.');

  if (isLocalHost) {
    netBadge.className = 'net-badge lan';
    netBadge.title = 'Bạn đang kết nối trực tiếp qua Mạng nội bộ LAN (Tốc độ tối đa ~1Gbps)';
    netText.textContent = 'Mạng LAN (Siêu tốc)';
    netBadge.href = 'javascript:void(0)';
    if (lanBanner) lanBanner.hidden = true;
  } else {
    netBadge.className = 'net-badge wan';
    netBadge.title = 'Bạn đang kết nối qua Cloudflare Tunnel (Internet). Bấm để chuyển sang Mạng nội bộ nếu đang ở nhà.';
    netText.textContent = 'Cloudflare WAN';
    netBadge.href = 'http://192.168.88.230:8080';
    netBadge.target = '_blank';

    if (!sessionStorage.getItem('dismissLanBanner')) {
      if (lanBanner) lanBanner.hidden = false;
    }
  }

  if (dismissBtn && lanBanner) {
    dismissBtn.addEventListener('click', () => {
      lanBanner.hidden = true;
      sessionStorage.setItem('dismissLanBanner', '1');
    });
  }
}

// Utility
function escapeHtml(str = '') {
  return String(str).replace(/[&<>"']/g, m => ({
    '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;'
  })[m]);
}

// =========================================================================
// REALTIME SERVER-SENT EVENTS (SSE)
// Cập nhật giao diện tức thì khi có file mới tải lên hoặc thay đổi
// =========================================================================
let realtimeDebounceTimer = null;

function handleVaultImportProgress(data) {
  const card = document.querySelector('#vaultProgressCard');
  const percentEl = document.querySelector('#vpcPercent');
  const barEl = document.querySelector('#vpcBarFill');
  const countEl = document.querySelector('#vpcCount');
  const nameEl = document.querySelector('#vpcFileName');

  if (!card) return;

  if (data.done) {
    if (percentEl) percentEl.textContent = '100%';
    if (barEl) barEl.style.width = '100%';
    if (nameEl) nameEl.textContent = 'Hoàn tất!';
    showToast(`✅ Đã chuyển thành công toàn bộ ${data.total} mục vào Thư mục bí mật!`, 'success');
    setTimeout(() => {
      card.hidden = true;
      loadFiles();
      fetchStorage();
    }, 2500);
    return;
  }

  card.hidden = false;
  if (percentEl) percentEl.textContent = `${data.percent}%`;
  if (barEl) barEl.style.width = `${data.percent}%`;
  if (countEl) countEl.textContent = `${data.current} / ${data.total}`;
  if (nameEl) nameEl.textContent = data.currentFile || 'Đang mã hóa...';
}

function initRealtimeEvents() {
  try {
    const evtSource = new EventSource(api('/api/events'));
    evtSource.addEventListener('file_changed', () => {
      clearTimeout(realtimeDebounceTimer);
      realtimeDebounceTimer = setTimeout(() => {
        loadFiles();
        fetchStorage();
      }, 100);
    });

    evtSource.addEventListener('vault_import_progress', (e) => {
      try {
        const data = JSON.parse(e.data);
        handleVaultImportProgress(data);
      } catch (_) {}
    });

    evtSource.onerror = () => {
      // Tự động kết nối lại
    };
  } catch (e) {
    console.warn('Realtime SSE không khả dụng:', e);
  }
}

// =========================================================================
// AUTHENTICATION & USER MANAGEMENT
// =========================================================================
let currentUser = null;

function showLoginModal(errorMessage = '') {
  const modal = document.querySelector('#loginModal');
  const banner = document.querySelector('#loginErrorBanner');
  if (!modal) return;
  modal.hidden = false;
  if (stageLoadingBar) stageLoadingBar.hidden = true;
  if (banner) {
    if (errorMessage) {
      banner.textContent = errorMessage;
      banner.hidden = false;
    } else {
      banner.hidden = true;
    }
  }
  const input = document.querySelector('#loginUsername');
  if (input) setTimeout(() => input.focus(), 50);
}

function hideLoginModal() {
  const modal = document.querySelector('#loginModal');
  if (modal) modal.hidden = true;
  const banner = document.querySelector('#loginErrorBanner');
  if (banner) banner.hidden = true;
}

function renderUserInfo(user) {
  if (!user) return;
  const initial = (user.full_name || user.username || 'AD').trim().slice(0, 2).toUpperCase();
  const avatarText = document.querySelector('#userAvatarText');
  const nameText = document.querySelector('#userNameText');
  const menuAvatar = document.querySelector('#userMenuAvatar');
  const menuFullName = document.querySelector('#userMenuFullName');
  const menuRole = document.querySelector('#userMenuRole');

  if (avatarText) avatarText.textContent = initial;
  if (nameText) nameText.textContent = user.username;
  if (menuAvatar) menuAvatar.textContent = initial;
  if (menuFullName) menuFullName.textContent = user.full_name || user.username;
  if (menuRole) menuRole.textContent = `@${user.username} • ${user.role === 'admin' ? 'Quản trị viên' : 'Thành viên'}`;
}

function initAuthUI() {
  // Toggle hiện / ẩn mật khẩu tại màn hình đăng nhập
  const togglePassBtn = document.querySelector('#toggleLoginPasswordBtn');
  const passInput = document.querySelector('#loginPassword');
  const eyeShow = document.querySelector('#eyeShowIcon');
  const eyeHide = document.querySelector('#eyeHideIcon');

  if (togglePassBtn && passInput) {
    togglePassBtn.addEventListener('click', () => {
      const isPass = passInput.type === 'password';
      passInput.type = isPass ? 'text' : 'password';
      if (eyeShow) eyeShow.hidden = isPass;
      if (eyeHide) eyeHide.hidden = !isPass;
    });
  }

  // Xử lý gửi Form Đăng nhập
  const loginForm = document.querySelector('#loginForm');
  const loginSubmitBtn = document.querySelector('#loginSubmitBtn');
  const loginErrorBanner = document.querySelector('#loginErrorBanner');

  if (loginForm) {
    loginForm.addEventListener('submit', async (e) => {
      e.preventDefault();
      const usernameInput = document.querySelector('#loginUsername');
      const username = usernameInput ? usernameInput.value.trim() : '';
      const password = passInput ? passInput.value : '';

      if (!username || !password) {
        if (loginErrorBanner) {
          loginErrorBanner.textContent = 'Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu';
          loginErrorBanner.hidden = false;
        }
        return;
      }

      const btnText = loginSubmitBtn ? loginSubmitBtn.querySelector('.btn-text') : null;
      const btnSpinner = loginSubmitBtn ? loginSubmitBtn.querySelector('.btn-spinner') : null;

      if (btnText) btnText.textContent = 'Đang xác thực...';
      if (btnSpinner) btnSpinner.hidden = false;
      if (loginSubmitBtn) loginSubmitBtn.disabled = true;
      if (loginErrorBanner) loginErrorBanner.hidden = true;

      try {
        const res = await fetch(api('/api/auth/login'), {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          credentials: 'include',
          body: JSON.stringify({ username, password })
        });
        const data = await res.json();

        if (data.ok && data.token) {
          setAuthToken(data.token);
          currentUser = data.user;
          renderUserInfo(currentUser);
          hideLoginModal();
          // Tải dữ liệu lưu trữ và danh sách tệp
          fetchStorage();
          loadFiles();
          setTimeout(initRealtimeEvents, 1000);
        } else {
          throw new Error(data.error || 'Đăng nhập không thành công');
        }
      } catch (err) {
        if (loginErrorBanner) {
          loginErrorBanner.textContent = err.message || 'Lỗi kết nối máy chủ';
          loginErrorBanner.hidden = false;
        }
      } finally {
        if (btnText) btnText.textContent = 'Đăng nhập';
        if (btnSpinner) btnSpinner.hidden = true;
        if (loginSubmitBtn) loginSubmitBtn.disabled = false;
      }
    });
  }

  // User Dropdown Menu
  const userPillBtn = document.querySelector('#userPillBtn');
  const userDropdownContainer = document.querySelector('.user-dropdown-container');
  const userDropdownMenu = document.querySelector('#userDropdownMenu');

  if (userPillBtn && userDropdownContainer && userDropdownMenu) {
    userPillBtn.addEventListener('click', (e) => {
      e.stopPropagation();
      const isHidden = userDropdownMenu.hidden;
      userDropdownMenu.hidden = !isHidden;
      userDropdownContainer.classList.toggle('open', !isHidden);
    });

    document.addEventListener('click', (e) => {
      if (!userDropdownContainer.contains(e.target)) {
        userDropdownMenu.hidden = true;
        userDropdownContainer.classList.remove('open');
      }
    });
  }

  // Đăng xuất
  const btnLogout = document.querySelector('#btnLogout');
  if (btnLogout) {
    btnLogout.addEventListener('click', async () => {
      try {
        await authFetch(api('/api/auth/logout'), { method: 'POST' });
      } catch (_) {}
      setAuthToken('');
      window.location.reload();
    });
  }

  // Đổi mật khẩu
  const btnOpenChangePassword = document.querySelector('#btnOpenChangePassword');
  const changePasswordModal = document.querySelector('#changePasswordModal');
  const cancelChangePasswordBtn = document.querySelector('#cancelChangePasswordBtn');
  const confirmChangePasswordBtn = document.querySelector('#confirmChangePasswordBtn');
  const oldPasswordInput = document.querySelector('#oldPasswordInput');
  const newPasswordInput = document.querySelector('#newPasswordInput');
  const confirmPasswordInput = document.querySelector('#confirmPasswordInput');
  const changePasswordErrorBanner = document.querySelector('#changePasswordErrorBanner');

  if (btnOpenChangePassword && changePasswordModal) {
    btnOpenChangePassword.addEventListener('click', () => {
      if (userDropdownMenu) userDropdownMenu.hidden = true;
      if (userDropdownContainer) userDropdownContainer.classList.remove('open');
      if (oldPasswordInput) oldPasswordInput.value = '';
      if (newPasswordInput) newPasswordInput.value = '';
      if (confirmPasswordInput) confirmPasswordInput.value = '';
      if (changePasswordErrorBanner) changePasswordErrorBanner.hidden = true;
      changePasswordModal.hidden = false;
      if (oldPasswordInput) oldPasswordInput.focus();
    });

    if (cancelChangePasswordBtn) {
      cancelChangePasswordBtn.addEventListener('click', () => {
        changePasswordModal.hidden = true;
      });
    }

    if (confirmChangePasswordBtn) {
      confirmChangePasswordBtn.addEventListener('click', async () => {
        const oldPass = oldPasswordInput ? oldPasswordInput.value : '';
        const newPass = newPasswordInput ? newPasswordInput.value : '';
        const confirmPass = confirmPasswordInput ? confirmPasswordInput.value : '';

        if (!oldPass || !newPass) {
          if (changePasswordErrorBanner) {
            changePasswordErrorBanner.textContent = 'Vui lòng nhập mật khẩu hiện tại và mật khẩu mới';
            changePasswordErrorBanner.hidden = false;
          }
          return;
        }

        if (newPass.length < 6) {
          if (changePasswordErrorBanner) {
            changePasswordErrorBanner.textContent = 'Mật khẩu mới phải từ 6 ký tự trở lên';
            changePasswordErrorBanner.hidden = false;
          }
          return;
        }

        if (newPass !== confirmPass) {
          if (changePasswordErrorBanner) {
            changePasswordErrorBanner.textContent = 'Xác nhận mật khẩu mới không trùng khớp';
            changePasswordErrorBanner.hidden = false;
          }
          return;
        }

        confirmChangePasswordBtn.disabled = true;
        confirmChangePasswordBtn.textContent = 'Đang lưu...';
        if (changePasswordErrorBanner) changePasswordErrorBanner.hidden = true;

        try {
          const res = await authFetch(api('/api/auth/change-password'), {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ oldPassword: oldPass, newPassword: newPass })
          });
          const data = await res.json();
          if (data.ok) {
            changePasswordModal.hidden = true;
            alert('Đổi mật khẩu thành công!');
          } else {
            throw new Error(data.error || 'Đổi mật khẩu thất bại');
          }
        } catch (err) {
          if (changePasswordErrorBanner) {
            changePasswordErrorBanner.textContent = err.message || 'Lỗi đổi mật khẩu';
            changePasswordErrorBanner.hidden = false;
          }
        } finally {
          confirmChangePasswordBtn.disabled = false;
          confirmChangePasswordBtn.textContent = 'Lưu thay đổi';
        }
      });
    }
  }
}

// =========================================================================
// VAULT FUNCTIONS & ENCRYPTION ENGINE UI
// =========================================================================
function updateToolbarForMode() {
  const toolbarImportDriveBtn = document.querySelector('#toolbarImportDriveBtn');
  if (state.isVault) {
    if (toolbarNewFolderBtn) toolbarNewFolderBtn.hidden = true;
    if (menuNewFolder) menuNewFolder.hidden = true;
    if (toolbarLockVaultBtn) toolbarLockVaultBtn.hidden = false;
    if (toolbarVaultChangePwdBtn) toolbarVaultChangePwdBtn.hidden = false;
    if (toolbarImportDriveBtn) toolbarImportDriveBtn.hidden = false;
  } else {
    if (toolbarNewFolderBtn) toolbarNewFolderBtn.hidden = false;
    if (menuNewFolder) menuNewFolder.hidden = false;
    if (toolbarLockVaultBtn) toolbarLockVaultBtn.hidden = true;
    if (toolbarVaultChangePwdBtn) toolbarVaultChangePwdBtn.hidden = true;
    if (toolbarImportDriveBtn) toolbarImportDriveBtn.hidden = true;
  }
}

function updateVaultBadge(isUnlocked) {
  if (!vaultChipStatus) return;
  if (isUnlocked) {
    vaultChipStatus.textContent = 'Mở';
    vaultChipStatus.classList.add('unlocked');
  } else {
    vaultChipStatus.textContent = 'Khóa';
    vaultChipStatus.classList.remove('unlocked');
  }
}

function renderVaultLockedView(isConfigured) {
  foldersSection.hidden = true;
  filesSection.hidden = false;
  emptyState.hidden = true;

  const filesHeader = filesSection.querySelector('.section-header-row');
  if (filesHeader) filesHeader.hidden = true;

  foldersGrid.innerHTML = '';
  filesGrid.innerHTML = `
    <div class="empty-state vault-locked-hero" style="grid-column: 1 / -1; padding: 48px 24px; text-align: center;">
      <div style="width: 80px; height: 80px; margin: 0 auto 20px; border-radius: 50%; background: rgba(244, 63, 94, 0.12); display: flex; align-items: center; justify-content: center; box-shadow: 0 0 24px rgba(244, 63, 94, 0.2);">
        <svg viewBox="0 0 24 24" style="width: 44px; height: 44px; fill: #f43f5e;"><path d="M18 8h-1V6c0-2.76-2.24-5-5-5S7 3.24 7 6v2H6c-1.1 0-2 .9-2 2v10c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V10c0-1.1-.9-2-2-2zm-6 9c-1.1 0-2-.9-2-2s.9-2 2-2 2 .9 2 2-.9 2-2 2zm3.1-9H8.9V6c0-1.71 1.39-3.1 3.1-3.1 1.71 0 3.1 1.39 3.1 3.1v2z"/></svg>
      </div>
      <h2 style="font-size: 20px; font-weight: 600; color: var(--text-primary); margin-bottom: 8px;">
        ${isConfigured ? 'Thư mục bí mật đang khóa' : 'Chưa thiết lập Thư mục bí mật'}
      </h2>
      <p style="font-size: 14px; color: var(--text-secondary); max-width: 480px; margin: 0 auto 24px; line-height: 1.5;">
        ${isConfigured
          ? 'Thư mục này được bảo vệ an toàn bằng mật khẩu riêng. Nhập mật khẩu để mở khóa và xem các tệp đã ẩn.'
          : 'Hãy thiết lập mật khẩu bảo vệ riêng cho Thư mục bí mật.'}
      </p>
      <button type="button" id="unlockVaultHeroBtn" class="btn-primary" style="background: linear-gradient(135deg, #f43f5e, #e11d48); padding: 10px 28px; border-radius: 9999px; font-weight: 600; font-size: 14px; box-shadow: 0 4px 14px rgba(244, 63, 94, 0.35); cursor: pointer;">
        ${isConfigured ? 'Mở khóa Thư mục bí mật' : 'Thiết lập mật khẩu Thư mục bí mật'}
      </button>
    </div>
  `;

  const heroBtn = document.querySelector('#unlockVaultHeroBtn');
  if (heroBtn) {
    heroBtn.addEventListener('click', () => openVaultUnlockModal(isConfigured));
  }
}

let isVaultConfiguredCached = false;

function openVaultUnlockModal(forceConfigured) {
  if (!vaultAuthModal) return;
  const isConfig = (typeof forceConfigured === 'boolean') ? forceConfigured : isVaultConfiguredCached;

  if (isConfig) {
    vaultModalTitle.textContent = 'Mở khóa Thư mục bí mật';
    vaultModalDesc.textContent = 'Dữ liệu được mã hóa chuẩn quân đội AES-256-GCM. Nhập mật khẩu để tiếp tục.';
    vaultConfirmGroup.hidden = true;
    confirmVaultAuthBtn.textContent = 'Mở khóa';
  } else {
    vaultModalTitle.textContent = 'Thiết lập Thư mục bí mật';
    vaultModalDesc.textContent = 'Tạo mật khẩu riêng cho Thư mục bí mật. Mật khẩu này bảo vệ chìa khóa AES-256-GCM, hãy ghi nhớ thật kỹ!';
    vaultConfirmGroup.hidden = false;
    confirmVaultAuthBtn.textContent = 'Khởi tạo Thư mục bí mật';
  }

  if (vaultModalErrorBanner) vaultModalErrorBanner.hidden = true;
  if (vaultPasswordInput) vaultPasswordInput.value = '';
  if (vaultConfirmPasswordInput) vaultConfirmPasswordInput.value = '';
  vaultAuthModal.hidden = false;
  setTimeout(() => { if (vaultPasswordInput) vaultPasswordInput.focus(); }, 50);
}

function initVaultUI() {
  if (navVaultLink) {
    navVaultLink.addEventListener('dragover', (e) => {
      e.preventDefault();
      e.dataTransfer.dropEffect = 'move';
      navVaultLink.classList.add('vault-drag-hover');
    });
    navVaultLink.addEventListener('dragleave', () => {
      navVaultLink.classList.remove('vault-drag-hover');
    });
    navVaultLink.addEventListener('drop', async (e) => {
      e.preventDefault();
      navVaultLink.classList.remove('vault-drag-hover');
      try {
        const raw = e.dataTransfer.getData('application/json');
        if (raw) {
          const parsed = JSON.parse(raw);
          if (parsed && parsed.paths && parsed.paths.length > 0) {
            await moveToVault(parsed.paths);
            return;
          }
        }
      } catch (_) {}
    });
  }

  const toolbarImportDriveBtn = document.querySelector('#toolbarImportDriveBtn');
  const pickDriveModal = document.querySelector('#pickDriveModal');
  const drivePickerContent = document.querySelector('#drivePickerContent');
  const drivePickerCountText = document.querySelector('#drivePickerCountText');
  const cancelPickDriveBtn = document.querySelector('#cancelPickDriveBtn');
  const confirmPickDriveBtn = document.querySelector('#confirmPickDriveBtn');
  let drivePickerSelected = new Set();

  async function openDrivePickerModal() {
    if (!pickDriveModal || !drivePickerContent) return;
    drivePickerSelected.clear();
    updatePickerConfirmState();
    drivePickerContent.innerHTML = '<div style="text-align: center; padding: 24px; color: var(--text-muted);">Đang nạp danh sách tệp từ Drive...</div>';
    pickDriveModal.hidden = false;

    try {
      const res = await authFetch(api('/api/files?folder='));
      const data = await res.json();
      if (!data.ok || !data.items || data.items.length === 0) {
        drivePickerContent.innerHTML = '<div style="text-align: center; padding: 24px; color: var(--text-muted);">Không có tệp hoặc thư mục nào trên Drive để chuyển.</div>';
        return;
      }

      drivePickerContent.innerHTML = '';
      for (const item of data.items) {
        const row = document.createElement('div');
        row.className = 'drive-picker-row';
        row.innerHTML = `
          <input type="checkbox" class="drive-picker-checkbox" data-path="${escapeHtml(item.path)}">
          <div class="drive-picker-icon">${SVG_ICONS[item.type] || SVG_ICONS.other}</div>
          <span class="drive-picker-name" title="${escapeHtml(item.name)}">${escapeHtml(item.name)}</span>
          <span class="drive-picker-meta">${item.formattedSize || (item.isFolder ? 'Thư mục' : '')}</span>
        `;
        row.addEventListener('click', (e) => {
          const cb = row.querySelector('.drive-picker-checkbox');
          if (e.target !== cb) {
            cb.checked = !cb.checked;
          }
          if (cb.checked) {
            drivePickerSelected.add(item.path);
          } else {
            drivePickerSelected.delete(item.path);
          }
          updatePickerConfirmState();
        });
        drivePickerContent.appendChild(row);
      }
    } catch (err) {
      drivePickerContent.innerHTML = `<div style="text-align: center; padding: 20px; color: #f43f5e;">Lỗi: ${escapeHtml(err.message)}</div>`;
    }
  }

  function updatePickerConfirmState() {
    if (drivePickerCountText) {
      drivePickerCountText.textContent = `Đã chọn ${drivePickerSelected.size} mục`;
    }
    if (confirmPickDriveBtn) {
      confirmPickDriveBtn.disabled = drivePickerSelected.size === 0;
    }
  }

  if (cancelPickDriveBtn) {
    cancelPickDriveBtn.addEventListener('click', () => {
      if (pickDriveModal) pickDriveModal.hidden = true;
    });
  }

  if (confirmPickDriveBtn) {
    confirmPickDriveBtn.addEventListener('click', async () => {
      if (drivePickerSelected.size === 0) return;
      const paths = Array.from(drivePickerSelected);
      if (pickDriveModal) pickDriveModal.hidden = true;
      await moveToVault(paths);
    });
  }

  if (toolbarImportDriveBtn) {
    toolbarImportDriveBtn.addEventListener('click', openDrivePickerModal);
  }
  if (toolbarLockVaultBtn) {
    toolbarLockVaultBtn.addEventListener('click', async () => {
      try {
        const res = await authFetch(api('/api/vault/lock'), { method: 'POST' });
        const data = await res.json();
        if (data.ok) {
          updateVaultBadge(false);
          loadFiles();
        }
      } catch (e) {
        console.error('Lỗi khóa két sắt:', e);
      }
    });
  }

  if (toolbarVaultChangePwdBtn) {
    toolbarVaultChangePwdBtn.addEventListener('click', () => {
      if (!vaultChangePwdModal) return;
      if (vaultChangePwdErrorBanner) vaultChangePwdErrorBanner.hidden = true;
      if (vaultOldPwdInput) vaultOldPwdInput.value = '';
      if (vaultNewPwdInput) vaultNewPwdInput.value = '';
      if (vaultConfirmNewPwdInput) vaultConfirmNewPwdInput.value = '';
      vaultChangePwdModal.hidden = false;
      setTimeout(() => { if (vaultOldPwdInput) vaultOldPwdInput.focus(); }, 50);
    });
  }

  if (cancelVaultAuthBtn) {
    cancelVaultAuthBtn.addEventListener('click', () => {
      pendingVaultImportPaths = null;
      if (vaultAuthModal) vaultAuthModal.hidden = true;
    });
  }

  if (confirmVaultAuthBtn) {
    confirmVaultAuthBtn.addEventListener('click', async () => {
      const pass = vaultPasswordInput ? vaultPasswordInput.value.trim() : '';
      if (!pass) {
        if (vaultModalErrorBanner) {
          vaultModalErrorBanner.textContent = 'Vui lòng nhập mật khẩu Két sắt';
          vaultModalErrorBanner.hidden = false;
        }
        return;
      }

      const isSetup = !vaultConfirmGroup.hidden;
      if (isSetup) {
        const confirmPass = vaultConfirmPasswordInput ? vaultConfirmPasswordInput.value.trim() : '';
        if (pass.length < 6) {
          if (vaultModalErrorBanner) {
            vaultModalErrorBanner.textContent = 'Mật khẩu phải từ 6 ký tự trở lên';
            vaultModalErrorBanner.hidden = false;
          }
          return;
        }
        if (pass !== confirmPass) {
          if (vaultModalErrorBanner) {
            vaultModalErrorBanner.textContent = 'Xác nhận mật khẩu không trùng khớp';
            vaultModalErrorBanner.hidden = false;
          }
          return;
        }
      }

      confirmVaultAuthBtn.disabled = true;
      confirmVaultAuthBtn.textContent = 'Đang xử lý...';
      if (vaultModalErrorBanner) vaultModalErrorBanner.hidden = true;

      try {
        const endpoint = isSetup ? '/api/vault/setup' : '/api/vault/unlock';
        const res = await authFetch(api(endpoint), {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ password: pass })
        });
        const data = await res.json();
        if (data.ok) {
          vaultAuthModal.hidden = true;
          isVaultConfiguredCached = true;
          updateVaultBadge(true);
          if (pendingVaultImportPaths && pendingVaultImportPaths.length > 0) {
            const pathsToImport = [...pendingVaultImportPaths];
            pendingVaultImportPaths = null;
            await executeVaultImport(pathsToImport);
          } else {
            loadFiles();
          }
        } else {
          throw new Error(data.error || 'Mật khẩu Két sắt không chính xác');
        }
      } catch (err) {
        if (vaultModalErrorBanner) {
          vaultModalErrorBanner.textContent = err.message || 'Lỗi xác thực Két sắt';
          vaultModalErrorBanner.hidden = false;
        }
      } finally {
        confirmVaultAuthBtn.disabled = false;
        confirmVaultAuthBtn.textContent = isSetup ? 'Khởi tạo Két sắt' : 'Mở khóa';
      }
    });
  }

  if (vaultPasswordInput) {
    vaultPasswordInput.addEventListener('keydown', (e) => {
      if (e.key === 'Enter') confirmVaultAuthBtn.click();
    });
  }
  if (vaultConfirmPasswordInput) {
    vaultConfirmPasswordInput.addEventListener('keydown', (e) => {
      if (e.key === 'Enter') confirmVaultAuthBtn.click();
    });
  }

  if (cancelVaultChangePwdBtn) {
    cancelVaultChangePwdBtn.addEventListener('click', () => {
      if (vaultChangePwdModal) vaultChangePwdModal.hidden = true;
    });
  }

  if (confirmVaultChangePwdBtn) {
    confirmVaultChangePwdBtn.addEventListener('click', async () => {
      const oldPassword = vaultOldPwdInput ? vaultOldPwdInput.value : '';
      const newPassword = vaultNewPwdInput ? vaultNewPwdInput.value : '';
      const confirmNew = vaultConfirmNewPwdInput ? vaultConfirmNewPwdInput.value : '';

      if (!oldPassword || !newPassword) {
        if (vaultChangePwdErrorBanner) {
          vaultChangePwdErrorBanner.textContent = 'Vui lòng nhập mật khẩu cũ và mới';
          vaultChangePwdErrorBanner.hidden = false;
        }
        return;
      }
      if (newPassword.length < 6) {
        if (vaultChangePwdErrorBanner) {
          vaultChangePwdErrorBanner.textContent = 'Mật khẩu mới phải từ 6 ký tự trở lên';
          vaultChangePwdErrorBanner.hidden = false;
        }
        return;
      }
      if (newPassword !== confirmNew) {
        if (vaultChangePwdErrorBanner) {
          vaultChangePwdErrorBanner.textContent = 'Xác nhận mật khẩu mới không trùng khớp';
          vaultChangePwdErrorBanner.hidden = false;
        }
        return;
      }

      confirmVaultChangePwdBtn.disabled = true;
      confirmVaultChangePwdBtn.textContent = 'Đang tái mã hóa...';
      if (vaultChangePwdErrorBanner) vaultChangePwdErrorBanner.hidden = true;

      try {
        const res = await authFetch(api('/api/vault/change-password'), {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ oldPassword, newPassword })
        });
        const data = await res.json();
        if (data.ok) {
          vaultChangePwdModal.hidden = true;
          alert('Đổi mật khẩu Két sắt và tái mã hóa toàn bộ tệp thành công!');
        } else {
          throw new Error(data.error || 'Đổi mật khẩu Két sắt thất bại');
        }
      } catch (err) {
        if (vaultChangePwdErrorBanner) {
          vaultChangePwdErrorBanner.textContent = err.message;
          vaultChangePwdErrorBanner.hidden = false;
        }
      } finally {
        confirmVaultChangePwdBtn.disabled = false;
        confirmVaultChangePwdBtn.textContent = 'Lưu & Tái mã hóa';
      }
    });
  }

  // Pre-check vault status for badge
  authFetch(api('/api/vault/status')).then(res => res.json()).then(data => {
    if (data.ok) {
      isVaultConfiguredCached = !!data.isConfigured;
      updateVaultBadge(!!data.isUnlocked);
    }
  }).catch(() => {});
}

async function checkAuthSession() {
  try {
    const res = await authFetch(api('/api/auth/me'));
    const data = await res.json();
    if (data.ok && data.user) {
      currentUser = data.user;
      renderUserInfo(currentUser);
      hideLoginModal();
      return true;
    }
  } catch (_) {}
  showLoginModal();
  return false;
}

// Initial Kickoff
initTheme();
initAuthUI();
initVaultUI();
setupGridEventDelegation(foldersGrid);
setupGridEventDelegation(filesGrid);
initNetworkDetection();

// Kiểm tra phiên đăng nhập hợp lệ trước khi tải dữ liệu
checkAuthSession().then(isAuthed => {
  if (isAuthed) {
    // Đọc URL query ?folder= nếu có để vào đúng thư mục ban đầu
    try {
      const urlParams = new URLSearchParams(window.location.search);
      const qFolder = urlParams.get('folder');
      if (qFolder) {
        state.currentFolder = decodeURIComponent(qFolder);
      }
      history.replaceState({
        folder: state.currentFolder,
        filter: state.currentFilter,
        isTrash: state.isTrash,
        isVault: state.isVault
      }, '', window.location.href);
    } catch (_) {}

    renderBreadcrumb();
    fetchStorage();
    loadFiles();
    setTimeout(initRealtimeEvents, 1000);
  }
});

// Tự động kiểm tra trạng thái vault mỗi 5 phút để cập nhật badge khi hết phiên
setInterval(async () => {
  if (!state.isVault) return;
  try {
    const res = await authFetch(api('/api/vault/status'));
    const data = await res.json();
    if (data.ok && !data.isUnlocked) {
      updateVaultBadge(false);
      // Vault đã tự khóa — thoát khỏi chế độ vault
      state.isVault = false;
      state.currentFolder = '';
      loadFiles();
      showToast('🔒 Thư mục bí mật đã tự khóa do hết thời gian chờ.', 'info');
    }
  } catch (_) {}
}, 5 * 60 * 1000);
// === BỔ SUNG TÍNH NĂNG: SẮP XẾP, CHỌN TẤT CẢ (CTRL+A), XOAY/ZOOM ẢNH & SỬA VĂN BẢN ===
(function initNasExtraFeatures() {
  const sortSelect = document.getElementById('sortSelect');
  const savedSort = localStorage.getItem('nas_sort_by') || 'name-asc';
  if (sortSelect) sortSelect.value = savedSort;

  function applySortItems() {
    if (!state || !Array.isArray(state.items)) return;
    const mode = (sortSelect && sortSelect.value) || localStorage.getItem('nas_sort_by') || 'name-asc';
    state.items.sort((a, b) => {
      if (!!a.isFolder !== !!b.isFolder) return a.isFolder ? -1 : 1;
      if (mode === 'name-desc') return String(b.name || '').localeCompare(String(a.name || ''), 'vi');
      if (mode === 'date-desc') return (new Date(b.mtime || b.updatedAt || 0) - new Date(a.mtime || a.updatedAt || 0));
      if (mode === 'date-asc') return (new Date(a.mtime || a.updatedAt || 0) - new Date(b.mtime || b.updatedAt || 0));
      if (mode === 'size-desc') return (Number(b.size || 0) - Number(a.size || 0));
      if (mode === 'size-asc') return (Number(a.size || 0) - Number(b.size || 0));
      return String(a.name || '').localeCompare(String(a.name || ''), 'vi');
    });
  }

  if (typeof renderItems === 'function') {
    const origRenderItems = renderItems;
    renderItems = function (...args) {
      applySortItems();
      return origRenderItems.apply(this, args);
    };
  }

  if (sortSelect) {
    sortSelect.addEventListener('change', () => {
      localStorage.setItem('nas_sort_by', sortSelect.value);
      if (typeof renderItems === 'function') renderItems();
    });
  }

  // Công cụ Xoay & Phóng to ảnh trong Image Modal
  const imgModalActions = document.querySelector('#imageModal .media-modal-actions');
  const imgPreview = document.getElementById('driveImagePreview');
  if (imgModalActions && imgPreview && !document.getElementById('imgRotateBtn')) {
    let zoom = 1, deg = 0;
    const updateTransform = () => { imgPreview.style.transform = `scale(${zoom}) rotate(${deg}deg)`; imgPreview.style.transition = 'transform 0.2s ease'; };
    const mkBtn = (id, txt, fn) => {
      const b = document.createElement('button');
      b.id = id; b.className = 'btn-media-download'; b.textContent = txt; b.type = 'button';
      b.addEventListener('click', fn);
      imgModalActions.insertBefore(b, imgModalActions.firstChild);
    };
    mkBtn('imgRotateBtn', '↻ Xoay', () => { deg = (deg + 90) % 360; updateTransform(); });
    mkBtn('imgZoomInBtn', '🔍+', () => { zoom = Math.min(4, +(zoom + 0.25).toFixed(2)); updateTransform(); });
    mkBtn('imgZoomOutBtn', '🔍-', () => { zoom = Math.max(0.5, +(zoom - 0.25).toFixed(2)); updateTransform(); });
    imgPreview.addEventListener('load', () => { zoom = 1; deg = 0; updateTransform(); });
  }

  // Chỉnh sửa & Lưu trực tiếp tệp văn bản trong Code Modal + Tạo tệp văn bản mới
  const codeActions = document.querySelector('#codeModal .media-modal-actions');
  const codeContent = document.getElementById('codeViewerContent');
  const codeTitle = document.getElementById('codeTitle');
  if (codeActions && codeContent && !document.getElementById('editCodeBtn')) {
    const editBtn = document.createElement('button');
    editBtn.id = 'editCodeBtn'; editBtn.className = 'btn-media-download'; editBtn.textContent = '✏️ Sửa & Lưu';
    editBtn.addEventListener('click', async () => {
      if (codeContent.isContentEditable) {
        const fileName = (codeTitle && codeTitle.textContent.trim()) || 'note.txt';
        const blob = new File([codeContent.innerText], fileName, { type: 'text/plain;charset=utf-8' });
        codeContent.contentEditable = 'false';
        codeContent.style.outline = 'none';
        editBtn.textContent = '✏️ Sửa & Lưu';
        if (typeof uploadFiles === 'function') await uploadFiles([blob]);
      } else {
        codeContent.contentEditable = 'true';
        codeContent.style.outline = '1px dashed var(--accent-indigo, #6366f1)';
        codeContent.focus();
        editBtn.textContent = '💾 Lưu lên NAS';
      }
    });
    codeActions.insertBefore(editBtn, codeActions.firstChild);
  }
})();
