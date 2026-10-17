// Подключение обработчика формы Add
function initAddUser() {
    const form = document.getElementById('addUserForm');

    form.addEventListener('submit', async (event) => {
        event.preventDefault();

        clearValidationErrors(addValidationFields);

        hideMessage('addUserMessage');

        const submitButton = form.querySelector('button[type="submit"]');

        submitButton.disabled = true;

        const roles = Array.from(
            document.getElementById('addRoles').selectedOptions
        ).map(option => option.value);

        const userData = {
            firstName: document.getElementById('addFirstName').value,
            lastName: document.getElementById('addLastName').value,
            age: Number(document.getElementById('addAge').value),
            email: document.getElementById('addEmail').value,
            password: document.getElementById('addPassword').value,
            roleDtoNames: roles
        };

        try {
            await apiFetch('/api/users', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(userData)
            });

            form.reset();
            await loadUsers();
            showAdminPanel(TabNames.ALL_USERS);
            showMessage('usersMessage', 'User added successfully', AlertType.SUCCESS);
        } catch (error) {
            if (error.validationErrors) {
                showValidationErrors(error.validationErrors, addValidationFields);
                return;
            }

            showMessage('addUserMessage', error.message, AlertType.DANGER);
            console.error(error);
        } finally {
            submitButton.disabled = false;
        }
    });
}

const addValidationFields = {
    firstName: 'addFirstName',
    lastName: 'addLastName',
    age: 'addAge',
    email: 'addEmail',
    password: 'addPassword',
    roleDtoNames: 'addRoles'
};