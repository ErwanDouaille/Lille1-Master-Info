/* ------------------------------
   $Id: current.c,v 1.1 2009/10/30 10:15:19 marquet Exp $
   ------------------------------------------------------------

   Access to $CURRENT_VOLUME and $HW_CONFIG
   Philippe Marquet, Oct 2009
   
*/

#include <stdlib.h>
#include <errno.h>

#include "tools.h"

extern int load_super(unsigned int vol);

#define DEFAULT_HW_CONFIG "hardware.ini"

unsigned int current_volume;

/* load super bloc of the $CURRENT_VOLUME
   set current_volume accordingly */
int
load_current_volume ()
{
    char* current_volume_str;
    int status;
    
    current_volume_str = getenv("CURRENT_VOLUME");
    if (! current_volume_str)
        return RETURN_FAILURE;

    errno = 0;
    current_volume = strtol(current_volume_str, NULL, 10);
    if (errno)
        return RETURN_FAILURE;
    
    status = load_super(current_volume);
    
    return status;
}

/* return hw_config filename */
char *
get_hw_config ()
{
    char* hw_config;

    hw_config = getenv("HW_CONFIG");
    return hw_config ? hw_config : DEFAULT_HW_CONFIG;
}
