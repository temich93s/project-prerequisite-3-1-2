function showTab(tabName) {
    const adminPanel = document.getElementById('adminPanel');
    const userPanel = document.getElementById('userPanel');

    const adminTab = document.getElementById('adminTab');
    const userTab = document.getElementById('userTab');

    adminPanel.classList.toggle('d-none', tabName !== 'admin');
    userPanel.classList.toggle('d-none', tabName !== 'user');

    adminTab.classList.toggle('active', tabName === 'admin');
    userTab.classList.toggle('active', tabName === 'user');
}

function initTabs(user) {
    const isAdmin = user.roleDtoNames.includes('ADMIN');

    const adminTab = document.getElementById('adminTab');

    adminTab.classList.toggle('d-none', !isAdmin);

    adminTab.addEventListener('click', () => {
        if (isAdmin) {
            showTab('admin');
        }
    });

    const userTab = document.getElementById('userTab')

    userTab.addEventListener('click', () => {
        showTab('user');
    });

    showTab(isAdmin ? 'admin' : 'user');
}