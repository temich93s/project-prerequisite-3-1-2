async function getCsrf() {
    const response = await fetch('/api/csrf');

    if (!response.ok) {
        throw new Error('Failed to get CSRF token');
    }

    return response.json();
}