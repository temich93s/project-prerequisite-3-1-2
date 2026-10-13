async function loadModals() {
    const files = [
        '/components/editUserModal.html',
        '/components/deleteUserModal.html'
    ];

    const container = document.getElementById('modalsContainer');

    for (const file of files) {
        const response = await fetch(file);

        if (!response.ok) {
            throw new Error(`Failed to load ${file}: ${response.status}`);
        }

        const html = await response.text();

        container.insertAdjacentHTML('beforeend', html);
    }
}