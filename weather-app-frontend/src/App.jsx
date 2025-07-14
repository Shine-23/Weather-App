import { useState, useEffect, useRef } from 'react';
import SearchBar from './components/SearchBar';
import WeatherDisplay from './components/WeatherDisplay';
import axios from 'axios';
import { Client } from '@stomp/stompjs';
import './App.css';

function App() {
    const [weatherList, setWeatherList] = useState([]);
    const stompClientRef = useRef(null);
    const [hasSearched, setHasSearched] = useState(false);
    const subscribedCitiesRef = useRef(new Set());  // To keep track of already subscribed cities

    const handleSearch = async (city) => {
        try {
            const response = await axios.get(`http://localhost:8080/api/weather?cityName=${city}`);
            const weatherData = response.data;

            setWeatherList((prevList) => {
                const exists = prevList.some(item => item.city.toLowerCase() === weatherData.city.toLowerCase());
                if (!exists) {
                    return [...prevList, weatherData];
                }
                return prevList;
            });

            setHasSearched(true);

            // Establish connection if not connected
            if (!stompClientRef.current || !stompClientRef.current.connected) {
                connectWebSocket(() => subscribeToCity(city));
            } else {
                subscribeToCity(city);
            }

        } catch (error) {
            console.error('Error fetching weather:', error);
        }
    };

    const connectWebSocket = (onConnectCallback) => {
        const client = new Client({
            brokerURL: 'http://localhost:8080/ws/weather',
            onConnect: () => {
                console.log('Connected to WebSocket');
                if (onConnectCallback) onConnectCallback();
            },
            debug: (str) => console.log(str),
        });

        client.activate();
        stompClientRef.current = client;
    };

    const subscribeToCity = (city) => {
        const normalizedCity = city.toLowerCase();
        if (subscribedCitiesRef.current.has(normalizedCity)) {
            return;  // Already subscribed
        }

        if (!stompClientRef.current || !stompClientRef.current.connected) {
            console.warn("WebSocket not connected yet.");
            return;
        }

        stompClientRef.current.subscribe(`/topic/weather/${normalizedCity}`, (message) => {
            const weatherUpdate = JSON.parse(message.body);
            console.log('Weather Update via WebSocket', weatherUpdate);
            setWeatherList((prevList) =>
                prevList.map(item =>
                    item.city.toLowerCase() === weatherUpdate.city.toLowerCase()
                        ? { ...weatherUpdate, updated: true }
                        : item
                )
            );
            setTimeout(() => {
                setWeatherList(prevList =>
                    prevList.map(item =>
                        item.city.toLowerCase() === weatherUpdate.city.toLowerCase()
                            ? { ...item, updated: false }
                            : item
                    )
                );
            }, 1000);
        });

        // Inform backend to start pushing updates for this city
        stompClientRef.current.publish({
            destination: '/app/subscribe',
            body: city
        });

        subscribedCitiesRef.current.add(normalizedCity);
    };

    const handleRemoveCity = (city) => {
        setWeatherList(prevList => prevList.filter(item => item.city.toLowerCase() !== city.toLowerCase()));
        
        const normalizedCity = city.toLowerCase();
        subscribedCitiesRef.current.delete(normalizedCity);

        // Optional: Inform backend to stop pushing updates
        if (stompClientRef.current && stompClientRef.current.connected) {
            stompClientRef.current.publish({
                destination: '/app/unsubscribe',
                body: city
            });
        }
    };

    useEffect(() => {
        return () => {
            if (stompClientRef.current) {
                stompClientRef.current.deactivate();
            }
        };
    }, []);

    return (
        <div className="container-fluid">
            <div className={`search-container ${hasSearched ? 'shrink' : ''}`}>
                <SearchBar onSearch={handleSearch} />
            </div>

            <div className="weather-grid">
                {weatherList.map((weather, index) => (
                    <WeatherDisplay key={index} weather={weather} onRemove={handleRemoveCity} />
                ))}
            </div>
        </div>
    );
}

export default App;
