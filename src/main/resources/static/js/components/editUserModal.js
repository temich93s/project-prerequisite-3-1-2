let selectedUserId = null;

// Открытие модального окна редактирования
function openEditModal(user) {
    selectedUserId = user.id;

    clearValidationErrors();

    const errorBox = document.getElementById('editError');
    errorBox.textContent = '';
    errorBox.classList.add('d-none');

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

        clearValidationErrors();

        const errorBox = document.getElementById('editError');
        errorBox.textContent = '';
        errorBox.classList.add('d-none');
        
        if (selectedUserId === null) {
            return;
        }

        const submitButton = form.querySelector('button[type="submit"]');

        submitButton.disabled = true;

        // Получаем выбранные роли
        const roles = Array.from(
            document.getElementById('editRoles').selectedOptions
        ).map(option => option.value);

        // Собираем данные пользователя
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
            const csrf = await getCsrf();

            const response = await fetch(
                `/api/users/${selectedUserId}`,
                {
                    method: 'PUT',
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
                        showValidationErrors(data);
                        return;
                    }

                    throw new Error(
                        data.detail || `Update failed: ${response.status}`
                    );
                }

                throw new Error(`Update failed: ${response.status}`);
            }

            bootstrap.Modal.getInstance(
                document.getElementById('editModal')
            ).hide();

            selectedUserId = null;

            // Обновляем таблицу
            await loadUsers();

            document.getElementById('message').textContent = 'User updated successfully';

        } catch (error) {
            console.error(error);
            document.getElementById('message').textContent = error.message;
        } finally {
            submitButton.disabled = false;
        }
    });
}

const validationFields = {
    firstName: 'editFirstName',
    lastName: 'editLastName',
    age: 'editAge',
    email: 'editEmail',
    roleDtoNames: 'editRoles'
};

function clearValidationErrors() {
    for (const [field, inputId] of Object.entries(validationFields)) {
        document.getElementById(`${field}Error`).textContent = '';
        document.getElementById(inputId).classList.remove('is-invalid');
    }
}

function showValidationErrors(errors) {
    for (const [field, message] of Object.entries(errors)) {
        const inputId = validationFields[field];
        if (!inputId) continue;

        document.getElementById(`${field}Error`).textContent = message;
        document.getElementById(inputId).classList.add('is-invalid');
    }
}
