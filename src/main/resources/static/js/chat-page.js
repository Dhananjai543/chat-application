'use strict';

document.addEventListener('DOMContentLoaded', function() {
	var logoutButton = document.getElementById('logoutButton');

    logoutButton.addEventListener('click', function() {
        fetch('/logout', { method: 'POST' })
            .then(() => window.location.href = '/showLoginForm');
    });
});