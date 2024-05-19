const express = require('express');
const bodyParser = require('body-parser');
const mysql = require('mysql');

const app = express();

const port = 3000;

// Middleware to enable CORS
app.use((req, res, next) => {
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE');
  res.setHeader('Access-Control-Allow-Headers', 'Content-Type, Authorization');
  next();
});

// MySQL connection configuration
const connection = mysql.createConnection({
  host: 'localhost',
  user: 'root',
  password: '1234',
  database: 'Website'
});

// Connect to MySQL
connection.connect((err) => {
  if (err) {
    console.error('Error connecting to MySQL database: ', err);
    return;
  }
  console.log('Connected to MySQL database');
});

// Parse URL-encoded bodies (as sent by HTML forms)
app.use(bodyParser.urlencoded({ extended: true }));

// Parse JSON bodies (as sent by API clients)
app.use(bodyParser.json());

// Handle payment form submission
// Handle payment form submission
app.post('/charge', (req, res) => {
  console.log('Received payment request');
  console.log('Request body:', req.body); // Log the entire request body

  const { stripeToken, name, email, amount, shipping_name, shipping_address, shipping_city, shipping_state, shipping_zip, shipping_country } = req.body || {};
  const stripeTokenData = JSON.parse(req.body.stripeTokenData || '{}'); // Extract additional data from Stripe token

  if (!stripeToken || !name || !email || !amount || !shipping_name || !shipping_address || !shipping_city || !shipping_state || !shipping_zip || !shipping_country || !stripeTokenData) {
      console.error('Missing required data in request body');
      return res.status(400).json({ error: 'Missing required data in request body' });
  }

  const { card, client_ip } = stripeTokenData;
  const { last4, brand, exp_month, exp_year } = card;

  const payment_status = 'successful'; // In test mode, consider all payments successful

  const sql = 'INSERT INTO cust_details (stripe_token, name, email, exp_month, exp_year, last4, brand, amount, client_ip, payment_status, shipping_name, shipping_address, shipping_city, shipping_state, shipping_zip, shipping_country) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)';
  const values = [stripeToken, name, email, exp_month, exp_year, last4, brand, amount, client_ip, payment_status, shipping_name, shipping_address, shipping_city, shipping_state, shipping_zip, shipping_country];

  connection.query(sql, values, (err, result) => {
      if (err) {
          console.error('Error saving payment details to database: ', err);
          return res.status(500).json({ error: 'Error processing payment' });
      }
      console.log('Payment details saved to database');

      return res.status(200).json({ message: 'Payment successful' });
  });
});

// Handle data fetching using POST method
app.post('/fetchDataFromServer', (req, res) => {
  const sql = 'SELECT * FROM cust_details'; // Modify query as per your table structure
  connection.query(sql, (err, result) => {
      if (err) {
          console.error('Error fetching data from database: ', err);
          return res.status(500).json({ error: 'Error fetching data' });
      }
      console.log('Data fetched successfully');
      return res.status(200).json(result); // Return JSON data
  });
});



// Start the server
app.listen(port, () => {
  console.log(`Server is running on port ${port}`);
});
