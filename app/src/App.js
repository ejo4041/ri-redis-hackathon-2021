import './App.css';
import Sidebar from './Sidebar/Sidebar';

import {
  BrowserRouter as Router,
  Switch,
  Route
} from "react-router-dom";
import Dashboard from './Dashboard/Dashboard';
import Login from './Auth/Login';
import Template from './Template/Template';
import Logout from './Auth/Logout';
import PrivateRoute from './Utils/PrivateRoute';
// import PublicRoute from './Utils/PublicRoute';
import { ThemeProvider } from '@material-ui/styles';
import { createMuiTheme } from '@material-ui/core';

const theme = createMuiTheme({
  palette: {
    primary: {
      main: '#E0D040',
    },
    secondary: {
      main: '#40E0D0',
    },
  },
});

function App() {
  return (
    <Router>
      <ThemeProvider theme={theme}>
        <div className="App">
          <Sidebar />
          <main>
            <Switch>
              <Route path="/login" component={Login} />
              <PrivateRoute exact path="/" component={Dashboard} />
              <PrivateRoute path="/template" component={Template} />
              <PrivateRoute path="/logout" component={Logout} />
            </Switch>
          </main>
        </div>
      </ThemeProvider>
    </Router>
  );
}

export default App;
