import {createTheme} from '@mui/material/styles';
export const makeTheme=(mode:'light'|'dark')=>createTheme({palette:{mode,primary:{main:'#00796b'},secondary:{main:'#f9a825'},background:{default:mode==='light'?'#f3f6f5':'#101716'}},shape:{borderRadius:10},typography:{fontFamily:'Inter, system-ui, sans-serif',h4:{fontWeight:750},h6:{fontWeight:700}},components:{MuiButton:{defaultProps:{disableElevation:true}},MuiCard:{styleOverrides:{root:{border:'1px solid',borderColor:mode==='light'?'#dde6e3':'#273936'}}}}});

