const ngrokUrl=' https://up-coyote-equally.ngrok-free.app';

document.addEventListener('DOMContentLoaded', function() {
    const paymentForm = document.getElementById('payment-form');
    paymentForm.setAttribute('action', `${ngrokUrl}/charge`);
});


document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM content loaded');

    
    // Set your publishable key
    var stripe = Stripe('pk_test_51Ow0i1SBksgpgavhIY07VMoikT02Li9RFdWJogSrJJPytUtPfETCaqPsSzuldJiMPhPdHJa9XL6p4VVChYEg8T1D009TrSrWZS');

    // Create an instance of elements
    var elements = stripe.elements();

    // Create an instance of the card Element
    var card = elements.create('card');

    // Add an instance of the card Element into the `card-element` div
    card.mount('#card-element');
    console.log('Card element mounted');

    // Handle form submission
    var form = document.getElementById('payment-form');
    form.addEventListener('submit', function(event) {
        event.preventDefault();
        

        stripe.createToken(card, {
            name: document.querySelector('input[name="name"]').value,
            address_line1: document.querySelector('input[name="shipping_address"]').value,
            address_city: document.querySelector('input[name="shipping_city"]').value,
            address_state: document.querySelector('input[name="shipping_state"]').value,
            address_zip: document.querySelector('input[name="shipping_zip"]').value,
            address_country: document.querySelector('input[name="shipping_country"]').value
        }).then(function(result) {
            if (result.error) {
                // Inform the user if there was an error
                var errorElement = document.getElementById('card-errors');
                errorElement.textContent = result.error.message;
            } else {
                // Send the token to your server
                var stripeTokenDataInput = document.getElementById('stripeTokenData');
                stripeTokenDataInput.value = JSON.stringify(result.token);
                stripeTokenHandler(result.token, form);

                
            }
        });
    });

    // Submit the form with the token ID
    function stripeTokenHandler(token, form) {
        var formData = new FormData(form);
        formData.append('stripeToken', token.id);

        // Convert FormData to JSON object
        var jsonObject = {};
        formData.forEach(function(value, key){
            jsonObject[key] = value;
        });

        // Send JSON object to server
        fetch(`${ngrokUrl}/charge`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(jsonObject)
        })
        .then(response => response.json())
        .then(data => {
            console.log(data);
            // Handle response data as needed
            alert("successful");
        
        })
        .catch(error => {
            console.error('Error:', error);
            alert("payment failed");
        });
    }
});

document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM content loaded');

    // Add event listener to fetch data button
    document.getElementById('fetchDataButton').addEventListener('click', fetchDataButton);
});

// Function to fetch and display data
// Function to fetch and display data
// Function to fetch and display data
document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM content loaded');

    // Add event listener to fetch data button
    document.getElementById('fetchDataButton').addEventListener('click', fetchDataFromServer);
});

// Function to fetch and display data
function fetchDataFromServer() {
    // Fetch data from your server using POST method
    fetch(`${ngrokUrl}/fetchDataFromServer`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        // Optionally, include request body if needed
        // body: JSON.stringify({ key: value })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Failed to fetch data');
        }
        return response.json(); // Parse response as JSON
    })
    .then(data => {
        console.log('Fetched data:', data); // Log fetched data
        // Display HTML response in a popup window
        openPopupWindow(formatDataIntoTable(data));
    })
    .catch(error => {
        console.error('Error fetching data:', error);
        alert('Failed to fetch data');
    });
}


// Function to format data into table
function formatDataIntoTable(data) {
    let tableHTML = '<table>';
    // Add table headers
    tableHTML += '<tr>';
    Object.keys(data[0]).forEach(key => {
        tableHTML += `<th>${key}</th>`;
    });
    tableHTML += '</tr>';
    // Add table rows
    data.forEach(row => {
        tableHTML += '<tr>';
        Object.values(row).forEach(value => {
            tableHTML += `<td>${value}</td>`;
        });
        tableHTML += '</tr>';
    });
    tableHTML += '</table>';
    return tableHTML;
}

// Function to open popup window and display table
function openPopupWindow(table) {
    const popupWindow = window.open('', '_blank', 'width=600,height=400,scrollbars=yes,resizable=yes');
    popupWindow.document.write(table);
}
