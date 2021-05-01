import './sidebar.scss';
import { SettingsOutlined, ToggleOnOutlined } from '@material-ui/icons';
import Icon from '@material-ui/core/Icon';



function Sidebar() {
  return (
    <div className="Sidebar">
      <div className="SidebarIcon Rotate">
        <SettingsOutlined />
      </div>
      <div className="SidebarIcon">
        <ToggleOnOutlined />
      </div>
      <div className="SidebarIcon">
        <Icon>manage_accounts</Icon>
      </div>
      <div className="SidebarIcon">
        <Icon>admin_panel_settings</Icon>
      </div>
    </div>
  );
}

export default Sidebar;
