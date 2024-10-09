
import React from 'react';
import { useState } from "react";
import TextField from '@mui/material/TextField';
import Stack from '@mui/material/Stack';
import Button from '@mui/material/Button';
import axios from 'axios';
import Swal from 'sweetalert2';

function AddProduct() {
    const [inputs, setInputs] = useState({
        name: "",
        price: "",
        stock: ""
    });

  const handleOnChange = (e) => {
    setInputs((prevState) => ({
      ...prevState,
      [e.target.name]: e.target.value,
    }));
    console.log("handleOnChange", inputs);
  };

  const handleSubmit = async (e) => {
        e.preventDefault();
        console.log("handleSubmit", inputs);
        const response = await axios.post(`http://localhost:8010/v1/products`, inputs);
        console.log("handleSubmit", response.data);
        if (response.data) {
            Swal.fire({
                icon: "success",
                title: "Product Added Successfully",
                text: `Product-Id: ${response.data}`,
            });
        }
   };

    return (
        <div>
            <form onSubmit={handleSubmit}>
                <Stack direction="row" alignItems="center" spacing={5}>
                    <h1>Add Product</h1>
                </Stack>
                <Stack direction="row" alignItems="center" spacing={5}>
                    <TextField 
                    id="standard-basic" 
                    label="Name" 
                    variant="standard" 
                    value={inputs.name} 
                    name="name" 
                    onChange={handleOnChange} 
                    />
                </Stack>
                <Stack direction="row" alignItems="center" spacing={5}>
                    <TextField 
                    id="standard-basic" 
                    label="Price" 
                    variant="standard" 
                    value={inputs.price} 
                    name="price" 
                    onChange={handleOnChange} 
                    />
                </Stack>
                <Stack direction="row" alignItems="center" spacing={5}>
                    <TextField 
                    id="standard-basic" 
                    label="Stock" 
                    variant="standard" 
                    value={inputs.stock} 
                    name="stock" 
                    onChange={handleOnChange} 
                    />
                </Stack>
                <Stack direction="row" alignItems="center" spacing={5}>
                    <br />
                </Stack>
                <Stack direction="row" alignItems="center" spacing={5}>
                    <Button variant="contained" type="submit">Add</Button>
                </Stack>
            </form>
        </div>
    );
}

export default AddProduct;