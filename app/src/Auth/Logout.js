import React from 'react';
import { removeSession } from '../Utils/Common';
 
function Logout(props) {
    console.log("logout");
    removeSession();
    props.history.push('/login');

    return(
        <div></div>
    )
}
 
export default Logout;