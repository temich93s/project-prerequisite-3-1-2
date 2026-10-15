async function main() {
    await initLogout();

    const user = await loadCurrentUser();

    initTabs(user);
    renderCurrentUser(user);

    if (user.roles.includes('ADMIN')) {
        initAdminPanel();

        await loadUsers();
        await loadModals();
        await loadRoles();

        initAddUser();
        initEditModal();
        initDeleteModal();
    }
}

main().catch(console.error);