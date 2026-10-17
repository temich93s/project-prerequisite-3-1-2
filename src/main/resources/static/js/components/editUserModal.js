let selectedUserId = null;

// Открытие модального окна редактирования
function openEditModal(user) {
    selectedUserId = user.id;

    clearValidationErrors(editValidationFields);

    hideMessage('editError');

    document.getElementById('editId').value = user.id;
    document.getElementById('editFirstName').value = user.firstName;
    document.getElementById('editLastName').value = user.lastName;
    document.getElementById('editAge').value = user.age;
    document.getElementById('editEmail').value = user.email;
    document.getElementById('editPassword').value = '';

    const roles = document.getElementById('editRoles');

    for (const option of roles.options) {
        option.selected = user.roleDtoNames.includes(option.value);
    }

    bootstrap.Modal.getOrCreateInstance(document.getElementById('editModal')).show();
}

// Подключение обработчика формы Edit
function initEditModal() {
    const form = document.getElementById('editUserForm');

    form.addEventListener('submit', async (event) => {
        event.preventDefault();

        clearValidationErrors(editValidationFields);

        hideMessage('editError');
        
        if (selectedUserId === null) {
            return;
        }

        const submitButton = form.querySelector('button[type="submit"]');

        submitButton.disabled = true;

        const roles = Array.from(
            document.getElementById('editRoles').selectedOptions
        ).map(option => option.value);

        const userData = {
            firstName: document.getElementById('editFirstName').value,
            lastName: document.getElementById('editLastName').value,
            age: Number(document.getElementById('editAge').value),
            email: document.getElementById('editEmail').value,
            roleDtoNames: roles
        };

        const password = document.getElementById('editPassword').value;

        // Передаём пароль, только если он изменён
        if (password !== '') {
            userData.password = password;
        }

        try {
            await apiFetch(`/api/users/${selectedUserId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(userData)
            });

        } catch (error) {
            if (error.validationErrors) {
                showValidationErrors(error.validationErrors, editValidationFields);
            } else {
                showMessage('editError', error.message, AlertType.DANGER);
                console.error(error);
            }

            submitButton.disabled = false;
            return;
        }

        // Обновляем таблицу после успешного обновления пользователя
        try {
            await loadUsers();
            showMessage('usersMessage', 'User updated successfully', AlertType.SUCCESS);

        } catch (error) {
            showMessage('usersMessage', 'User updated, but table refresh failed', AlertType.WARNING);
            console.error(error);
        }

        // Обновляем шапку с указанием текущего пользователя
        const currentUser = await loadCurrentUser();
        renderCurrentUser(currentUser);

        // закрываем модальное окно
        selectedUserId = null;
        submitButton.disabled = false;
        bootstrap.Modal.getInstance(document.getElementById('editModal')).hide();
    });
}

const editValidationFields = {
    firstName: 'editFirstName',
    lastName: 'editLastName',
    age: 'editAge',
    email: 'editEmail',
    roleDtoNames: 'editRoles'
};
