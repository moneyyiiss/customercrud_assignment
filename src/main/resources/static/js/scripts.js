document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('loginForm');
    const registerForm = document.getElementById('registerForm');
    const addCustomerForm = document.getElementById('addCustomerForm');
    const editCustomerForm = document.getElementById('editCustomerForm');
    const syncButton = document.getElementById('syncButton');
    const searchButton = document.getElementById('searchButton');
    const customerTable = document.getElementById('customerTable');
    const paginationContainer = document.getElementById('pagination');
    const loading = document.getElementById('loading');

    let currentPage = 0;
    const pageSize = 10;

    // Event listener for login form submission
    if (loginForm) {
        loginForm.addEventListener('submit', function(event) {
            event.preventDefault();
            const username = document.getElementById('username').value;
            const password = document.getElementById('password').value;
            login(username, password);
        });
    }

    // Event listener for register form submission
    if (registerForm) {
        registerForm.addEventListener('submit', function(event) {
            event.preventDefault();
            const username = document.getElementById('username').value;
            const password = document.getElementById('password').value;
            register(username, password);
        });
    }

    // Event listener for add customer form submission
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

    // Event listener for edit customer form submission
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

    // Event listener for sync button
    if (syncButton) {
        syncButton.addEventListener('click', function() {
            syncCustomers();
        });
    }

    // Event listener for search button
    if (searchButton) {
        searchButton.addEventListener('click', function() {
            searchCustomers();
        });
    }

    // Function to handle login
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

    // Function to handle registration
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

    // Function to handle adding a customer
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

    // Function to handle updating a customer
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

    // Function to load customers with optional search parameters
    function loadCustomers(queryParam = '', queryValue = '') {
        let url = `/api/customers?page=${currentPage}&size=${pageSize}`;
        if (queryParam && queryValue) {
            url += `&searchBy=${queryParam}&searchValue=${queryValue}`;
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

            // Update pagination
            updatePagination(data.totalPages);
        })
        .catch(error => console.error('Error:', error));
    }

    // Function to handle editing a customer
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

    // Function to handle deleting a customer
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

    // Function to handle syncing customers from a remote API
    function syncCustomers() {
        loading.style.display = 'block'; // Show loading

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
        .finally(() => {
            loading.style.display = 'none'; // Hide loading
        })
        .catch(error => {
            console.error('Error:', error);
            loading.style.display = 'none'; // Hide loading
        });
    }

    // Function to handle searching customers
    function searchCustomers() {
        const searchBy = document.getElementById('searchBy').value;
        const searchInput = document.getElementById('searchInput').value;
        loadCustomers(searchBy, searchInput);
    }

    // Function to update pagination
    function updatePagination(totalPages) {
        paginationContainer.innerHTML = '';

        const prevButton = document.createElement('button');
        prevButton.textContent = 'Previous';
        prevButton.disabled = currentPage === 0;
        prevButton.addEventListener('click', function() {
            if (currentPage > 0) {
                currentPage--;
                loadCustomers();
            }
        });
        paginationContainer.appendChild(prevButton);

        const nextButton = document.createElement('button');
        nextButton.textContent = 'Next';
        nextButton.disabled = currentPage === totalPages - 1;
        nextButton.addEventListener('click', function() {
            if (currentPage < totalPages - 1) {
                currentPage++;
                loadCustomers();
            }
        });
        paginationContainer.appendChild(nextButton);
    }

    // Initial load of customers if customerTable is present
    if (customerTable) {
        loadCustomers();
    }
});
