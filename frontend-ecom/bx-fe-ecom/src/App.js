import React from 'react';
import './App.css';
import Container from '@mui/material/Container';
import CssBaseline from '@mui/material/CssBaseline';
import AddProduct from './components/products/AddProduct';

function App() {
  
  return (
    <div className="App">
      <CssBaseline />
      <Container maxWidth="sm">
        <AddProduct />
      </Container>
    </div>
  );
}

export default App;
