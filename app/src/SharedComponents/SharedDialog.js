import { Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle } from '@material-ui/core'

export default function SharedDialog(props) {
      return (
        <>
            <Dialog
                open={props.open}
                onClose={props.onClose}
                aria-labelledby="alert-dialog-title"
                aria-describedby="alert-dialog-description"
            >
                <DialogTitle id="alert-dialog-title">{props.title}</DialogTitle>
                <DialogContent>
                    <DialogContentText id="alert-dialog-description">
                        {props.body}
                    </DialogContentText>
                </DialogContent>
                <DialogActions >
                    <Button onClick={props.onCancel} color="secondary">
                        {props.cancelText}
                    </Button>
                    <Button onClick={props.onAccept} color="primary" autoFocus>
                        {props.acceptText}
                    </Button>
                </DialogActions>
            </Dialog>
        </>
    )
}