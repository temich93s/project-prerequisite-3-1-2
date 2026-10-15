async function loadRoles() {
    const response = await fetch('/api/roles');

    if (response.redirected || !response.ok) {
        throw new Error(`Failed to load roles: ${response.status}`);
    }

    const roles = await response.json();

    roles.sort();

    fillRolesSelect('addRoles', roles);
    fillRolesSelect('editRoles', roles);
}

function fillRolesSelect(selectId, roles) {
    const select = document.getElementById(selectId);

    select.replaceChildren();

    for (const role of roles) {
        const option = document.createElement('option');

        option.value = role;
        option.textContent = role;

        select.appendChild(option);
    }
}