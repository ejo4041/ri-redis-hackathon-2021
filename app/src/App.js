import './App.css';
import Sidebar from './Sidebar/Sidebar';

import {
  BrowserRouter as Router,
  Switch,
  Route
} from "react-router-dom";
import Dashboard from './Dashboard/Dashboard';
import Login from './Auth/Login';
import TemplateList from './Template/TemplateList';

const routes = [
  {
    path: "/",
    exact: true,
    main: Dashboard
  },
  {
    path: "/template",
    main: TemplateList
  },
  {
    path: "/user",
    main: () => <h2>Users Coming Soon</h2>
  },
  {
    path: "/login",
    main: Login
  }
];


function App() {
  return (
    <Router>

      <div className="App">
        <Sidebar />
        <main>
          <Switch>
            {routes.map((route, index) => (
              <Route
                key={index}
                path={route.path}
                exact={route.exact}
                children={<route.main />}
              />
            ))}
          </Switch>
        </main>
      </div>
    </Router>
  );
}

export default App;
