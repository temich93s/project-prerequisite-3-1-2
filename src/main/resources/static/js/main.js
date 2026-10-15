async function main() {
    await initLogout();

    const user = await loadCurrentUser();

    initTabs(user);
    renderCurrentUser(user);

    if (user.roles.includes('ADMIN')) {
        await loadModals();

        initEditModal();
        initDeleteModal();

        await loadUsers();
    }
}

main().catch(console.error);