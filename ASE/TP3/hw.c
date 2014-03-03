/* ------------------------------
   $Id: hw.c,v 1.5 2006/10/24 07:21:59 marquet Exp $
   ------------------------------------------------------------

   Basic hardware emulation.
   Philippe Marquet, Dec 2003
   
*/

#include <sys/time.h>
#include <signal.h>
#include <stdio.h>
#include <assert.h>

#include "hw.h"

/* a 8 milliseconds timer */
#define TIMER_SEC  0
#define TIMER_USEC 8000

static irq_handler_func_t *timer_irq_handler = (irq_handler_func_t *) 0; 
static int irqs_are_enable = 1;

static void
do_timer_interrupt()
{
    if (timer_irq_handler && irqs_are_enable)
	timer_irq_handler();
}

void
start_hw()
{
    {
	/* start timer handler */
	static struct sigaction sa;
	
	sigemptyset(&sa.sa_mask);
	sa.sa_handler = do_timer_interrupt;
	sa.sa_flags = SA_RESTART | SA_NODEFER;
	sigaction(SIGALRM, &sa, (struct sigaction *)0);
    }

    {
	/* set timer */ 
	struct itimerval value;

	/* timer period */
	value.it_interval.tv_sec = TIMER_SEC;
	value.it_interval.tv_usec = TIMER_USEC;
	/* first deliverable */
	value.it_value = value.it_interval; 
	setitimer(ITIMER_REAL, &value, (struct itimerval *)0);
    }
}

void
setup_irq(unsigned int irq, irq_handler_func_t handler)
{
    assert(irq == TIMER_IRQ);
    timer_irq_handler = handler;
}

void
irq_disable()
{
    irqs_are_enable = 0; 
}

void
irq_enable()
{
    irqs_are_enable = 1; 
}
