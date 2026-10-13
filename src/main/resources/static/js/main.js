async function main() {
    await loadModals();

    initEditModal();
    initDeleteModal();

    await loadUsers();
    await loadCurrentUser();
    await initLogout();
}

main().catch(console.error);