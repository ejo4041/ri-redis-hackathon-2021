import './sidebar.scss';
import { SettingsOutlined } from '@material-ui/icons';
import LibraryAddOutlinedIcon from '@material-ui/icons/LibraryAddOutlined';
import ExitToAppOutlinedIcon from '@material-ui/icons/ExitToAppOutlined';
import { Link } from 'react-router-dom';
import { Tooltip } from '@material-ui/core';


export default function Sidebar() {
  return (
    <div className="Sidebar">
      <Tooltip title="Dash" placement="right">
        <Link to="/" className="SidebarIcon Rotate">
            <SettingsOutlined />
        </Link>
      </Tooltip>
      <Tooltip title="Templates" placement="right">
        <Link to="/template" className="SidebarIcon">
            <LibraryAddOutlinedIcon />
        </Link>
      </Tooltip>
      <Tooltip title="Logout" placement="right">
        <Link to="/logout" className="SidebarIcon">
            <ExitToAppOutlinedIcon />
        </Link>
      </Tooltip>
      {/* <Tooltip title="Users" placement="right">
        <Link to="/user" className="SidebarIcon">
            <Icon>manage_accounts</Icon>
        </Link>
      </Tooltip>
      <Tooltip title="Admin Settings" placement="right">
        <Link to="/admin" className="SidebarIcon">
            <Icon>admin_panel_settings</Icon>
        </Link>
      </Tooltip> */}
    </div>
  );
}
