import './App.css';
import Sidebar from './context/Sidebar';
import { SettingsOutlined } from '@material-ui/icons';

function App() {
  return (
    <div className="App">
      <Sidebar />
      <main>
        <div className="App-logo">
          <SettingsOutlined />
          Config-As-A-Service
        </div>
      </main>
    </div>
  );
}

export default App;
