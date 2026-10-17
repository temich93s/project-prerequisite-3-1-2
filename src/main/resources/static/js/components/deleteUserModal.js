let deleteUserId = null;

// Открытие модального окна удаления
function openDeleteModal(user) {
    deleteUserId = user.id;

    hideMessage('deleteError');

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

        hideMessage('deleteError');

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
        } catch (error) {
            showMessage('deleteError', error.message, AlertType.DANGER);
            deleteButton.disabled = false;
            console.error(error);
            return;
        }

        // Обновляем таблицу после успешного удаления пользователя
        try {
            await loadUsers();
            showMessage('usersMessage', 'User deleted successfully', AlertType.SUCCESS);

        } catch (error) {
            showMessage('usersMessage', 'User deleted, but table refresh failed', AlertType.WARNING);
            console.error(error);
        }

        // закрываем модальное окно
        deleteUserId = null;
        deleteButton.disabled = false;
        bootstrap.Modal.getInstance(document.getElementById('deleteModal')).hide();
    });
}