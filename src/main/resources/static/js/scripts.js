document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('loginForm');
    const registerForm = document.getElementById('registerForm');
    const addCustomerForm = document.getElementById('addCustomerForm');
    const editCustomerForm = document.getElementById('editCustomerForm');
    const syncButton = document.getElementById('syncButton');
    const searchButton = document.getElementById('searchButton');
    const customerTable = document.getElementById('customerTable');

    if (loginForm) {
        loginForm.addEventListener('submit', function(event) {
            event.preventDefault();
            const username = document.getElementById('username').value;
            const password = document.getElementById('password').value;
            login(username, password);
        });
    }

    if (registerForm) {
        registerForm.addEventListener('submit', function(event) {
            event.preventDefault();
            const username = document.getElementById('username').value;
            const password = document.getElementById('password').value;
            register(username, password);
        });
    }

    if (addCustomerForm) {
        addCustomerForm.addEventListener('submit', function(event) {
            event.preventDefault();
            const customerData = {
                firstName: document.getElementById('firstName').value,
                lastName: document.getElementById('lastName').value,
                street: document.getElementById('street').value,
                address: document.getElementById('address').value,
                city: document.getElementById('city').value,
                state: document.getElementById('state').value,
                email: document.getElementById('email').value,
                phone: document.getElementById('phone').value
            };
            addCustomer(customerData);
        });
    }

    if (editCustomerForm) {
        editCustomerForm.addEventListener('submit', function(event) {
            event.preventDefault();
            const customerData = {
                id: document.getElementById('customerId').value,
                firstName: document.getElementById('firstName').value,
                lastName: document.getElementById('lastName').value,
                street: document.getElementById('street').value,
                address: document.getElementById('address').value,
                city: document.getElementById('city').value,
                state: document.getElementById('state').value,
                email: document.getElementById('email').value,
                phone: document.getElementById('phone').value
            };
            updateCustomer(customerData);
        });

        // Populate the edit form with data from localStorage
        document.getElementById('customerId').value = localStorage.getItem('editCustomerId');
        document.getElementById('firstName').value = localStorage.getItem('editCustomerFirstName');
        document.getElementById('lastName').value = localStorage.getItem('editCustomerLastName');
        document.getElementById('street').value = localStorage.getItem('editCustomerStreet');
        document.getElementById('address').value = localStorage.getItem('editCustomerAddress');
        document.getElementById('city').value = localStorage.getItem('editCustomerCity');
        document.getElementById('state').value = localStorage.getItem('editCustomerState');
        document.getElementById('email').value = localStorage.getItem('editCustomerEmail');
        document.getElementById('phone').value = localStorage.getItem('editCustomerPhone');
    }

    if (syncButton) {
        syncButton.addEventListener('click', function() {
            syncCustomers();
        });
    }

    if (searchButton) {
        searchButton.addEventListener('click', function() {
            searchCustomers();
        });
    }

    function login(username, password) {
        fetch('/api/authenticate', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username, password })
        })
        .then(response => response.text())
        .then(data => {
            localStorage.setItem('token', data);
            window.location.href = '/customers.html';
        })
        .catch(error => console.error('Error:', error));
    }

    function register(username, password) {
        fetch('/api/signup', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username, password })
        })
        .then(response => response.text())
        .then(data => {
            alert(data);  // "User registered successfully"
            window.location.href = '/index.html';
        })
        .catch(error => console.error('Error:', error));
    }

    function addCustomer(customerData) {
        fetch('/api/customers', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + localStorage.getItem('token')
            },
            body: JSON.stringify(customerData)
        })
        .then(response => response.json())
        .then(data => {
            window.location.href = '/customers.html';
        })
        .catch(error => console.error('Error:', error));
    }

    function updateCustomer(customerData) {
        fetch(`/api/customers/${customerData.id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + localStorage.getItem('token')
            },
            body: JSON.stringify(customerData)
        })
        .then(response => response.json())
        .then(data => {
            window.location.href = '/customers.html';
        })
        .catch(error => console.error('Error:', error));
    }

    function loadCustomers(queryParam = '', queryValue = '') {
        let url = '/api/customers';
        if (queryParam && queryValue) {
            url += `?searchBy=${queryParam}&searchValue=${queryValue}`;
        }

        fetch(url, {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('token')
            }
        })
        .then(response => response.json())
        .then(data => {
            const tbody = customerTable.querySelector('tbody');
            tbody.innerHTML = '';
            data.content.forEach(customer => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${customer.id}</td>
                    <td>${customer.firstName}</td>
                    <td>${customer.lastName}</td>
                    <td>${customer.street}</td>
                    <td>${customer.address}</td>
                    <td>${customer.city}</td>
                    <td>${customer.state}</td>
                    <td>${customer.email}</td>
                    <td>${customer.phone}</td>
                    <td>
                        <button class="edit-button" data-id="${customer.id}">Edit</button>
                        <button class="delete-button" data-id="${customer.id}">Delete</button>
                    </td>
                `;
                tbody.appendChild(row);
            });

            // Attach event listeners to the dynamically created buttons
            document.querySelectorAll('.edit-button').forEach(button => {
                button.addEventListener('click', function() {
                    const customerId = this.getAttribute('data-id');
                    editCustomer(customerId);
                });
            });

            document.querySelectorAll('.delete-button').forEach(button => {
                button.addEventListener('click', function() {
                    const customerId = this.getAttribute('data-id');
                    deleteCustomer(customerId);
                });
            });
        })
        .catch(error => console.error('Error:', error));
    }

    function editCustomer(id) {
        fetch(`/api/customers/${id}`, {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('token')
            }
        })
        .then(response => response.json())
        .then(customer => {
            localStorage.setItem('editCustomerId', customer.id);
            localStorage.setItem('editCustomerFirstName', customer.firstName);
            localStorage.setItem('editCustomerLastName', customer.lastName);
            localStorage.setItem('editCustomerStreet', customer.street);
            localStorage.setItem('editCustomerAddress', customer.address);
            localStorage.setItem('editCustomerCity', customer.city);
            localStorage.setItem('editCustomerState', customer.state);
            localStorage.setItem('editCustomerEmail', customer.email);
            localStorage.setItem('editCustomerPhone', customer.phone);
            window.location.href = '/editCustomer.html';
        })
        .catch(error => console.error('Error:', error));
    }

    function deleteCustomer(id) {
        fetch(`/api/customers/${id}`, {
            method: 'DELETE',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('token')
            }
        })
        .then(() => {
            alert('Customer deleted successfully');
            loadCustomers();
        })
        .catch(error => console.error('Error:', error));
    }

    function syncCustomers() {
        fetch('/api/customers/sync', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + localStorage.getItem('token')
            },
            body: JSON.stringify({
                username: "test@sunbasedata.com",
                password: "Test@123"
            })
        })
        .then(response => response.text())
        .then(data => {
            alert(data);
            loadCustomers();
        })
        .catch(error => console.error('Error:', error));
    }

    function searchCustomers() {
        const searchBy = document.getElementById('searchBy').value;
        const searchInput = document.getElementById('searchInput').value;
        loadCustomers(searchBy, searchInput);
    }

    if (customerTable) {
        loadCustomers();
    }
});
