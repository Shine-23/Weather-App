import { Card} from 'react-bootstrap';
import './WeatherDisplay.css'

function WeatherDisplay({ weather, onRemove }) {
    if (!weather) return null;

    return (
        <Card className={`weather-display animate-weather ${weather.updated ? 'updated' : ''}`}>
        
             <div className="remove-btn" onClick={() => onRemove(weather.city)}>X</div>
            <Card.Body>
                <Card.Title className="weather-title">{weather.city}, {weather.country}</Card.Title>
                <Card.Text className="weather-text">
                    🌡️ <strong>Temperature:</strong> {weather.temperature}°C<br />
                    🤗 <strong>Feels Like:</strong> {weather.feelsLike}°C<br />
                    ☁️ <strong>Description:</strong> {weather.description}<br />
                    💧 <strong>Humidity:</strong> {weather.humidity}%<br />
                    🌬️ <strong>Wind Speed:</strong> {weather.windSpeed} m/s<br />
                </Card.Text>
                <img
                    src={`http://openweathermap.org/img/wn/${weather.icon}@2x.png`}
                    alt="weather icon"
                    className="weather-icon"
                />
            </Card.Body>
        </Card>
    );
}

export default WeatherDisplay;
