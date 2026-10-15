function showAdminPanel(tabName) {
    const allUsersPanelTabButton = document.getElementById('allUsersPanelTabButton');
    const addUserPanelTabButton = document.getElementById('addUserPanelTabButton');

    const allUsersPanelTab = document.getElementById('allUsersPanelTab');
    const addUserPanelTab = document.getElementById('addUserPanelTab');

    allUsersPanelTabButton.classList.toggle('active', tabName === TabNames.ALL_USERS);
    addUserPanelTabButton.classList.toggle('active', tabName === TabNames.ADD_USER);

    allUsersPanelTab.classList.toggle('d-none', tabName !== TabNames.ALL_USERS);
    addUserPanelTab.classList.toggle('d-none', tabName !== TabNames.ADD_USER);
}

function initAdminPanel() {
    const allUsersPanelTabButton = document.getElementById('allUsersPanelTabButton');
    const addUserPanelTabButton = document.getElementById('addUserPanelTabButton');

    allUsersPanelTabButton.addEventListener('click', () => {
        showAdminPanel(TabNames.ALL_USERS);
    });

    addUserPanelTabButton.addEventListener('click', () => {
        showAdminPanel(TabNames.ADD_USER);
    });

    showAdminPanel(TabNames.ALL_USERS);
}

const TabNames = Object.freeze({
    ALL_USERS: 'allUsers',
    ADD_USER: 'addUser'
});
