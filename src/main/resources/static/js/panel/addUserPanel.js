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
            const csrf = await getCsrf();

            const response = await fetch(
                `/api/users`,
                {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        [csrf.headerName]: csrf.token
                    },
                    body: JSON.stringify(userData)
                }
            );

            if (response.redirected) {
                throw new Error('Authentication required');
            }

            if (!response.ok) {
                const contentType = response.headers.get('content-type') || '';

                if (contentType.includes('json')) {
                    const data = await response.json();

                    if (response.status === 400 && !('detail' in data)) {
                        showValidationErrors(data, addValidationFields);
                        return;
                    }

                    throw new Error(
                        data.detail || `Add failed: ${response.status}`
                    );
                }

                throw new Error(`Add failed: ${response.status}`);
            }

            document.getElementById('addUserForm').reset();

            await loadUsers();
            showAdminPanel(TabNames.ALL_USERS);

            showMessage('usersMessage', 'User added successfully', AlertType.SUCCESS);
        } catch (error) {
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