// return the token from the session storage
export const getToken = () => {
    const token = sessionStorage.getItem('token');
    console.log("getToken -> " + token);
    return token || null;
}

export const getUser = () => {
    const token = sessionStorage.getItem('token');
    let user = JSON.parse(token).username;
    return user;
}

// remove the token from the session storage
export const removeSession = () => {
    sessionStorage.removeItem('token');
}

// set the token in the session storage
export const setToken = (token) => {
    sessionStorage.setItem('token', JSON.stringify(token));
}