function renderCurrentUser(user) {
    const tableBody = document.getElementById('currentUserTableBody');

    const tr = document.createElement('tr');

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

    tableBody.replaceChildren(tr);
}