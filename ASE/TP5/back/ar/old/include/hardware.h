/* ------------------------------
   $Id: hardware.h 78 2007-05-21 17:33:37Z skaczmarek $
   ------------------------------------------------------------

   hardware.h

   Interface de la biblioth�que de simulation du mat�riel. 

*/

#ifndef _HARDWARE_H_
#define _HARDWARE_H_

/**
 * CMD_
 * commandes ATA-2
 */
#define CMD_SEEK        0x02
#define CMD_READ        0x04
#define CMD_WRITE       0x06
#define CMD_FORMAT      0x08
#define CMD_STATUS      0x12
#define CMD_DMASET      0x14
#define CMD_DSKINFO     0x16
#define CMD_MANUF       0xA2
#define CMD_DIAG        0xA4

/* Alloc memory under mmu control */
extern void *tlb_malloc(unsigned size,unsigned unit_size);

/* Free memory under mmu control */
extern int tlb_free(void *area);

/* Make a TLB dump to stdout */
extern void tlb_dump();

/**
 * prototype des fonctions-interruptions.
 * une interruption ne recoit aucun param�tre "d'appel", 
 * une interruption ne retourne aucun resultat, mais
 * sa terminaison restaure le contexte d'ex�cution du programme interrompu. 
 */
typedef void (*func_irq)(void);

/**
 *      int init_hardware(const char *fileconfig);
 *          initialisation du mat�riel. Pas de "reinitialisation" possible.
 *          l'initialisation d�finit le mat�riel conform�ment aux sp�cifications
 *          fournies par le fichier dont le nom est "fileconfig".
 *          retourne 0 en cas de probl�me lors de l'initialisation.
 */ 
extern int init_hardware(const char *fileconfig);

/**
 *      IRQVECTOR 
 *          donne la base d'un tableau de pointeur de fonction du type
 *          func_irq. la fonction IRQVECTOR[n]() est appel�e lorsque 
 *          l'interuption de niveau n est d�clanch�e par le mat�riel.
 */
extern func_irq irq_vector[16];	/* n'utilisez pas cette variable    */
#define IRQVECTOR irq_vector	/* pr�f�rez ce #define IRQVECTOR    */

extern int SYSTICKDURATION;	/* microseconde entre les SYSTICK   */

/**
 * MASTERBUFFER et SLAVEBUFFER
 *     Adresses des buffers de donnees des disques maitre et esclave
 */
/* n'utilisez pas ces variables*/
extern unsigned char ** HDA_masterbufferaddress, **HDB_masterbufferaddress;
/* pr�f�rez ces #define MASTERBUFFER et SLAVEBUFFER */
#define MASTERBUFFER (*HDA_masterbufferaddress) 
#define SLAVEBUFFER  (*HDB_masterbufferaddress) 

/**
 *      BASEADDRESS_RAM
 *          variable associ�e � adresse de base de la m�moire globale
 *          de la machine. Cette m�moire est commune � tout les programmes 
 *          qui utilisent la librairie sur la m�me machine.  
 *          
 */
extern  unsigned char *baseGlobalMem;       /* n'utilisez pas cette variable */
#define BASEADDRESS_RAM baseGlobalMem /* pr�f�rez ce #define BASEADDRESS_RAM */

/**
 *      int _in(int port);
 *          lecture du contenu du registre mat�riel n� "port".
 *          retourne la valeur lue.
 */ 
int     _in(int port);

/**
 *      void _out(int port, int value);
 *          ecriture de la valeur "value" dans le registre mat�riel n� "port".
 */
void    _out(int port, int value);

/**
 *      void _sleep(int irq_level);
 *          Stoppe l'activit� du microprocesseur jusqu'� l'occurence
 *          une interruption de niveau au moins �gale � "irqLevel".
 */
void    _sleep(int irq_level);

/**
 *      void _mask(int irq_level);
 *          cache au microprocesseur l'occurence d'interruptions 
 *          de niveau au moins �gale � irqLevel.
 *
 */
void     _mask(int irq_level);

#endif

