async function apiFetch(url, options = {}) {
    const method = (options.method || 'GET').toUpperCase();

    const headers = new Headers(options.headers);

    if (!['GET', 'HEAD', 'OPTIONS'].includes(method)) {
        const csrf = await getCsrf();
        headers.set(csrf.headerName, csrf.token);
    }

    const response = await fetch(url, {
        ...options,
        headers
    });

    if (response.redirected) {
        throw new Error('Authentication required');
    }

    if (!response.ok) {
        const contentType = response.headers.get('content-type') || '';

        if (contentType.includes('json')) {
            const data = await response.json();

            if (response.status === 400 && !('detail' in data)) {
                const error = new Error('Validation failed');
                error.validationErrors = data;
                throw error;
            }

            throw new Error(
                data.detail || `Request failed: ${response.status}`
            );
        }

        if (response.status === 401) {
            throw new Error('Authentication required');
        }

        if (response.status === 403) {
            throw new Error('Access denied');
        }

        throw new Error(`Request failed: ${response.status}`);
    }

    return response;
}