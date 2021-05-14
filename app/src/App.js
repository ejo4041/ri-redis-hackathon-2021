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
import PublicRoute from './Utils/PublicRoute';

const routes = [
  {
    path: "/",
    exact: true,
    main: Dashboard
  },
  {
    path: "/template",
    main: Template
  },
  // {
  //   path: "/user",
  //   main: () => <h2>Users Coming Soon</h2>
  // },
  {
    path: "/login",
    main: Login
  },
  {
    path: "/logout",
    main: Logout
  }
];


function App() {
  return (
    <Router>

      <div className="App">
        <Sidebar />
        <main>
          <Switch>
            {/* {routes.map((route, index) => (
              <Route
                key={index}
                path={route.path}
                exact={route.exact}
                children={<route.main />}
              />
            ))} */}
            <Route path="/login" component={Login} />
            <PrivateRoute exact path="/" component={Dashboard} />
            <PrivateRoute path="/template" component={Template} />
            <PrivateRoute path="/logout" component={Logout} />
          </Switch>
        </main>
      </div>
    </Router>
  );
}

export default App;
