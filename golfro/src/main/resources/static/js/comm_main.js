document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('.post-link').forEach(function(link) {
        link.addEventListener('click', function(event) {
            event.preventDefault();
            const postId = link.getAttribute('data-id');

            fetch('/gaebokchi/community/increaseViews', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: new URLSearchParams({ 'id': postId })
            })
            .then(response => {
                if (response.ok) {
                    window.location.href = link.getAttribute('href');
                } else {
                    alert('Failed to increase views');
                }
            })
            .catch(error => {
                console.error('Error:', error);
            });
        });
    });
});
