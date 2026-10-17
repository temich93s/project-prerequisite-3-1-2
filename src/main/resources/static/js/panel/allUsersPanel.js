async function loadUsers() {
    const tableBody = document.getElementById('usersTableBody');

    try {
        const response = await apiFetch('/api/users');
        const users = await response.json();

        tableBody.replaceChildren();

        for (const user of users) {
            const tr = document.createElement('tr');

            // Данные пользователя
            const values = [
                user.id,
                user.firstName,
                user.lastName,
                user.age,
                user.email,
                user.roleDtoNames.join(', ')
            ];

            for (const value of values) {
                const td = document.createElement('td');
                td.textContent = value ?? '';
                tr.appendChild(td);
            }

            // Кнопка Edit
            const editTd = document.createElement('td');
            const editButton = document.createElement('button');

            editButton.type = 'button';
            editButton.className = 'btn btn-info text-white';
            editButton.textContent = 'Edit';

            editButton.addEventListener('click', () => {
                openEditModal(user);
            });

            editTd.appendChild(editButton);
            tr.appendChild(editTd);

            // Кнопка Delete
            const deleteTd = document.createElement('td');
            const deleteButton = document.createElement('button');

            deleteButton.type = 'button';
            deleteButton.className = 'btn btn-danger text-white';
            deleteButton.textContent = 'Delete';

            deleteButton.addEventListener('click', () => {
                openDeleteModal(user);
            });

            deleteTd.appendChild(deleteButton);
            tr.appendChild(deleteTd);

            tableBody.appendChild(tr);
        }

    } catch (error) {
        showMessage('usersMessage', 'Failed to load users', AlertType.DANGER);
        console.error(error);
        throw error;
    }
}