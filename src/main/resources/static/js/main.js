async function main() {
    await initLogout();

    const user = await loadCurrentUser();

    initTabs(user);
    renderCurrentUser(user);

    if (user.roleDtoNames.includes('ADMIN')) {
        initAdminPanel();

        await loadModals();
        await loadRoles();

        initAddUser();
        initEditModal();
        initDeleteModal();

        await loadUsers();
    }
}

main().catch(console.error);