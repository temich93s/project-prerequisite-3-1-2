let deleteUserId = null;

// Открытие модального окна удаления
function openDeleteModal(user) {
    deleteUserId = user.id;

    clearValidationErrors();

    const errorBox = document.getElementById('deleteError');
    errorBox.textContent = '';
    errorBox.classList.add('d-none');

    document.getElementById('deleteId').value = user.id;
    document.getElementById('deleteFirstName').value = user.firstName;
    document.getElementById('deleteLastName').value = user.lastName;
    document.getElementById('deleteAge').value = user.age;
    document.getElementById('deleteEmail').value = user.email;
    document.getElementById('deleteRoles').value = user.roleDtoNames.join(', ');

    bootstrap.Modal.getOrCreateInstance(document.getElementById('deleteModal')).show();
}

// Подключение обработчика кнопки Delete
function initDeleteModal() {
    const deleteButton = document.getElementById('confirmDeleteButton');

    deleteButton.addEventListener('click', async () => {
        clearValidationErrors();

        const errorBox = document.getElementById('deleteError');
        errorBox.textContent = '';
        errorBox.classList.add('d-none');

        if (deleteUserId === null) {
            return;
        }

        deleteButton.disabled = true;

        try {
            const csrf = await getCsrf();

            const response = await fetch(
                `/api/users/${deleteUserId}`,
                {
                    method: 'DELETE',
                    headers: {
                        [csrf.headerName]: csrf.token
                    }
                }
            );

            if (response.redirected) {
                throw new Error('Authentication required');
            }

            if (!response.ok) {
                const problem = await response.json();

                throw new Error(
                    problem.detail || `Delete failed: ${response.status}`
                );
            }

            bootstrap.Modal.getInstance(document.getElementById('deleteModal')).hide();

            deleteUserId = null;

            await loadUsers();

            document.getElementById('usersMessage').textContent = 'User deleted successfully';
        } catch (error) {
            console.error(error);
            document.getElementById('usersMessage').textContent = error.message;
        } finally {
            deleteButton.disabled = false;
        }
    });
}
