async function initLogout() {
    const csrf = await getCsrf();

    const input = document.getElementById('csrfToken');

    input.name = csrf.parameterName;
    input.value = csrf.token;
}

async function loadCurrentUser() {
    const response = await fetch('/api/auth/me');

    if (!response.ok) {
        throw new Error('Failed to load current user');
    }

    const user = await response.json();

    document.getElementById('currentUserEmail').textContent = user.email;
    document.getElementById('currentUserRoles').textContent = `[${user.roles.join(', ')}]`;

    return user;
}