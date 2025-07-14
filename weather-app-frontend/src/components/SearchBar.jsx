import { useState } from 'react';
import { Button, Form, InputGroup } from 'react-bootstrap';
import './SearchBar.css';

function SearchBar({ onSearch }) {
    const [city, setCity] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        if (city.trim() !== '') {
            onSearch(city);
            setCity('');
        }
    };

    return (
        <Form onSubmit={handleSubmit} className="search-bar">
            <InputGroup>
                <Form.Control
                    type="text"
                    placeholder="Enter city name"
                    value={city}
                    onChange={(e) => setCity(e.target.value)}
                />
                <Button variant="primary" type="submit">
                    Search
                </Button>
            </InputGroup>
        </Form>
    );
}

export default SearchBar;
