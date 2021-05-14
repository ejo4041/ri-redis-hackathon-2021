import { SettingsOutlined } from "@material-ui/icons"
import { getUser } from '../Utils/Common';

export default function Dashboard() {
    console.log("Dashboard");
    const user = getUser();
    console.log("User -> " + user);
    return (
        <div>
            <div className="App-logo">
                <SettingsOutlined />
                Config-As-A-Service
            </div>
            <h1>Welcome, {user}!</h1>
        </div>
    )
}