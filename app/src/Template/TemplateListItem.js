import { Box, Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, Tooltip } from '@material-ui/core'
import { Delete, DeleteForever } from '@material-ui/icons'
import { useState } from 'react';
import templateService from '../services/template.service'
import SharedDialog from '../SharedComponents/SharedDialog';

export default function TemplateListItem(props) {
    const [openDialog, setOpenDialog] = useState(false);

    const handleClickOpen = () => {
        setOpenDialog(true);
      };
    
      const handleClose = () => {
        setOpenDialog(false);
      };
      const handleDelete = () => {
          handleClose();
          props.deleteHandler(props.template.settingsId);
      }
      return (
        <>
            <Box 
                width={1} 
                display="flex" 
                alignItems="center"
                key={props.template.templateName} 
            >
                <Box flexGrow={1}
                p={2}>
                    {props.template.templateName}
                </Box>
                <Box flexGrow={0} mx={2} tooltip="Delete">
                    {props.selected && 
                        <Tooltip title="Delete Forever" placement="right">
                            <Button 
                                variant="contained" 
                                color="primary" 
                                disableElevation
                                onClick={handleClickOpen}
                            >
                                <DeleteForever />
                            </Button>
                        </Tooltip>}
                </Box>
            </Box>
            <SharedDialog 
                open={openDialog}
                onClose={handleClose}
                title={`Deleting ${props.template.templateName}`}
                body={`Are you sure you want to delete ${props.template.templateName}, FOREVER?`}
                onCancel={handleClose}
                cancelText={'Cancel'}
                onAccept={handleDelete}
                acceptText={'Delete'}
            />
            {/* <Dialog
                open={openDialog}
                onClose={handleClose}
                aria-labelledby="alert-dialog-title"
                aria-describedby="alert-dialog-description"
            >
                <DialogTitle id="alert-dialog-title">Deleting {props.template.templateName}</DialogTitle>
                <DialogContent>
                    <DialogContentText id="alert-dialog-description">
                        Are you sure you want to delete {props.template.templateName}, FOREVER?
                    </DialogContentText>
                </DialogContent>
                <DialogActions >
                    <Button onClick={handleClose} color="secondary">
                        Cancel
                    </Button>
                    <Button onClick={handleDelete} color="primary" autoFocus>
                        Delete
                    </Button>
                </DialogActions>
            </Dialog> */}
        </>
    )
}