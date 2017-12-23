:t
/PREMIUM_START/,/PREMIUM_END/ {   
   /PREMIUM_END/!{         
      $!{          
         N;        
         bt
      }            
   }               
   /.*/d;       
}
