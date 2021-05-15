import React, { useState } from 'react';
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import TextField from '@material-ui/core/TextField';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox from '@material-ui/core/Checkbox';
import Link from '@material-ui/core/Link';
import Grid from '@material-ui/core/Grid';
import Box from '@material-ui/core/Box';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import Typography from '@material-ui/core/Typography';
import { makeStyles } from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';

import { SettingsOutlined } from "@material-ui/icons"

import AuthService from '../services/auth.service';
import { setToken } from '../Utils/Common';

const useStyles = makeStyles((theme) => ({
  paper: {
    //marginTop: theme.spacing(0),
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
  },
  avatar: {
    margin: theme.spacing(1),
    backgroundColor: theme.palette.secondary.main,
  },
  form: {
    width: '100%', // Fix IE 11 issue.
    marginTop: theme.spacing(1),
  },
  submit: {
    margin: theme.spacing(3, 0, 2),
  },
}));

const useFormInput = initialValue => {
  const [value, setValue] = useState(initialValue);
 
  const handleChange = e => {
    setValue(e.target.value);
  }
  return {
    value,
    onChange: handleChange
  }
}

export default function Login(props) {
  console.log("Login");
  const classes = useStyles();

  const [loading, setLoading] = useState(false);
  const username = useFormInput('');
  const password = useFormInput('');
  const [error, setError] = useState(null);

  const handleLogin = (e) => {
    e.preventDefault();
    setError(null);
    setLoading(true);
    console.log("Username: " + username.value);
    console.log("Password: " + password.value);
    AuthService.login(username.value, password.value).then((data) => {
      console.log("Auth Response: " + JSON.stringify(data));
      setToken(data);
      props.history.push('/');
    }).catch(error => {
      setLoading(false);
      if (error && error.response && error.response.status === 401) {
        setError("Authentication Failed.  Please Try Again.");
      } else setError("An unexpected error occurred.  Please try again.");
    });
  }

  return (
    <Container component="main" maxWidth="xs">
      <CssBaseline />
      <div className={classes.paper}>
        {/* <Avatar className={classes.avatar}>
          <LockOutlinedIcon />
        </Avatar> */}
        <div className="App-logo">
          <SettingsOutlined />
        </div>
        <Typography component="h1" variant="h5">
          Config-As-A-Service
        </Typography>
        <form className={classes.form} noValidate onSubmit={handleLogin.bind(this)}>
          <TextField
            variant="outlined"
            margin="normal"
            required
            fullWidth
            id="email"
            label="Email Address"
            name="email"
            autoComplete="email"
            {...username}
            autoFocus
          />
          <TextField
            variant="outlined"
            margin="normal"
            required
            fullWidth
            name="password"
            label="Password"
            type="password"
            id="password"
            autoComplete="current-password"
            {...password}
          />
          <Button
            type="submit"
            fullWidth
            variant="contained"
            color="primary"
            className={classes.submit}
            onClick={handleLogin}
            disableElevation
          >
            Sign In
          </Button>
          {error && <><span style={{ color: 'red' }}>{error}</span><br /></>}<br />
        </form>
      </div>
    </Container>
  );
}