package service;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import utils.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import sun.util.calendar.BaseCalendar;

public class Limpeza {
    
    public static void main(String args[]){
        File file =  new File("C:\\LIMPA_ARQUIVOS");
        int horarioTermino = 11;
        int fim = -90;
        int inicio = -60;
        IniciarExclusao(file,horarioTermino,fim,inicio);         
    }
    
    /**Essa Função pega as informações de data necessárias,verifica se o Ano do prazo é o mesmo
     * e chama a respectiva função de exclusão*/
    public static void IniciarExclusao (File meuDiretorio,int horarioTermino,int fim, int inicio){        
        //Setando Data(Ano, Mes , Dia e Hora)
        int anoInicial = CalendarioUtils.getAno(inicio);
        int anoFim = CalendarioUtils.getAno(fim);        
        int mesInicial = CalendarioUtils.getMes(inicio);
        int mesFinal = CalendarioUtils.getMes(fim);
        int diaInicial = CalendarioUtils.getDia(inicio);
        int diaFinal = CalendarioUtils.getDia(fim);
        int mHora = CalendarioUtils.getHora(inicio);      
        
        //Verifica se o Prazo está dentro do mesmo Ano e
        //Chama a Função de exclusão que esteja dentro do mesmo ano
        //Ou em anos diferentes
        if(anoInicial == anoFim){
        File f = new File(meuDiretorio + "\\" + anoFim ); 
        limpezaDentroDoPrazo(f,mesFinal,mesInicial, diaInicial,diaFinal,mHora , horarioTermino);        
        }else{
         File f = new File(meuDiretorio+"\\" + anoFim ); 
        limpezaAnoFinal(f,mesFinal,diaFinal, mHora, horarioTermino); 
         File f2 = new File(meuDiretorio+ "\\" + anoInicial ); 
        limpezaAnoInicial(f2,mesInicial,diaInicial,mHora, horarioTermino);
        }
        
        //Debuga o prazo em que ocorreu a exclusão dos arquivos
        System.out.println("Deletando Arquivos que  estejam Entre ");
        System.out.println(diaFinal+" do "+mesFinal+" de "+anoFim + " após "+mHora +" Horas");
        System.out.println("E " );
        System.out.println(diaInicial+" do "+mesInicial+" de "+anoInicial + " antes das  "+mHora +" Horas" );
    }       
    
    /**Essa Função lista os arquivos dentro do prazo, entra nos subdiretórios e exclui o que estiver
     * dentro do prazo*/
    public static  void limpezaDentroDoPrazo(File diretorioRaiz, 
    int mMesFinal, int mMesInicial, int mDiaInicial, int mDiaFinal,int mHora , int horaTermino)  {
            File arquivo = diretorioRaiz;	                    
           //Lista os Diretórios
	    File[] subdirs = arquivo.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);
            for (File arquivoAtual : subdirs) {                   
            String dirName = String.valueOf(arquivoAtual.getName());
            int mMyDir = verificaMesDasPastas(dirName); 
            //Verifica se o Prazo está dentro do mesmo mês    
            if (mMesInicial == mMesFinal && mMyDir == mMesFinal){
                
                       File dirsDiasM =  new File(arquivo + "\\"+dirName);
                          File [] subDi = dirsDiasM.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);
                               //Entra na Pasta mes e Lista os Sub Dias
                                for (File arquivosNoMes : subDi){   
                                int diaD = Integer.parseInt(arquivosNoMes.getName()); 
                                    //Deleta os Diretórios que estejam dentro do prazo 
                                    if (diaD > mDiaFinal && diaD < mDiaInicial){
                                    DeletarDiretorios(arquivosNoMes);
                                    }
                                    //entra no dia Final e deleta as horas posteriores ao horario atual
                                    if(diaD == mDiaFinal){
                                    File diaF =  new File (dirsDiasM + "\\" + mDiaFinal);
                                    File[] subdia= diaF.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);
                                    for (File arquivosNoDIaF: subdia){ 
                                    int daHora = Integer.parseInt(arquivosNoDIaF.getName());
                                    if (daHora > mHora)
                                    DeletarDiretorios(arquivosNoDIaF);
                                    }
                                    }
                                    //entra no dia Inicial e deleta as horas anteriores ao horario Atual
                                    else if(diaD == mDiaInicial){
                                    File diaF1 =  new File (dirsDiasM + "\\" + mDiaInicial);
                                    File[] subdia= diaF1.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);
                                    for (File arquivosNoDIaF: subdia){ 
                                    int daHora1 = Integer.parseInt(arquivosNoDIaF.getName());
                                    if (daHora1 < mHora)
                                    DeletarDiretorios(arquivosNoDIaF);
                                }
                                 
                            }
                        }
                   }
                    else  if ( mHora < horaTermino){  
                    //Caso a pasta com o mes estiver dentro do prazo
                    //Entra no diretorio e verifica se o dia esta dentro do prazo       
                      File dirsDias =  new File(arquivo + "\\"+String.valueOf(dirName));
                      File [] subD = dirsDias.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);     
                            //Percorre os Arquivos que estão dentro do MesFinal e 
                            //Exclui os diretórios com os Dias fora do prazo
                            for (File arquivosNoPrazo : subD){   
                                if(mMyDir == mMesFinal && mHora < horaTermino){
                                int diaFinal = Integer.parseInt(arquivosNoPrazo.getName());                                    
                                if (diaFinal > mDiaFinal){
                                    DeletarDiretorios(arquivosNoPrazo);
                                }
                                //Entra nas Pastas do Dia e 
                                //Percorre as Pastas com as Horas
                                if (diaFinal == mDiaFinal && mHora < horaTermino ){
                                File dirsHoras = new File(dirsDias + "\\" + String.valueOf(diaFinal));
                                File [] subH = dirsHoras.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);
                                for (File dHoras : subH){
                                   int dirHora = Integer.valueOf(dHoras.getName());
                                   if (dirHora > mHora){
                                       DeletarDiretorios(dHoras);
                                       
                                   }                                
                                }
                                }
                          }
                          //Verifica os Dias do Mes Inicial
                                if ( mMyDir == mMesInicial && mHora < horaTermino){
                          int diaInicial = Integer.parseInt(arquivosNoPrazo.getName());                                
                                if (diaInicial < mDiaInicial){
                                    DeletarDiretorios(arquivosNoPrazo);
                                }
                                if(diaInicial == mDiaInicial){
                                File mHoras = new File(dirsDias + "\\" + String.valueOf(diaInicial));
                                File [] mDirHoras = mHoras.listFiles((FileFilter)DirectoryFileFilter.DIRECTORY);
                                for (File dirH : mDirHoras){
                                  int horaI = Integer.parseInt(dirH.getName());
                                  if(horaI < mHora){                                  
                                      DeletarDiretorios(dirH);
                                }
                            }
                        }                                
                    }
                }
                 
            }     
        }
                  
    }
    
    /**Essa Função lista os arquivos dentro do prazo, entra nos subdiretórios e exclui o que estiver
     * dentro do prazo caso o prazo possua dois anos EX: (Dez - Jan) funciona no ano mais Antigo*/    
    public static void limpezaAnoFinal(File diretorio, int mes, int dia, int hora, int horaTermino){
     File[] subdirsMes = diretorio.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);      
       for(File subsM : subdirsMes){
       String subMes = subsM.getName();
       int sMes = verificaMesDasPastas(subMes);
       if(horaTermino > hora && sMes == mes){
       File dias = new File(diretorio + "\\" + subMes);
       File [] subDias = dias.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);

       for(File diasSub : subDias){
       int diaS = Integer.parseInt(diasSub.getName().toString());        
       if(diaS > dia){
           DeletarDiretorios(diasSub);
       }else if(horaTermino > hora && diaS==dia){
       File horas = new File(dias + "\\" + dia);
       File [] subHoras = horas.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);
         for (File horaSub : subHoras){
         int horaS = Integer.parseInt(horaSub.getName());
          if(horaS > hora){
              DeletarDiretorios(horaSub);
          }
         }
       }
       
       }
       }    
       }
      
   
   }
  
    /**Essa Função lista os arquivos dentro do prazo, entra nos subdiretórios e exclui o que estiver
     * dentro do prazo caso o prazo possua dois anos EX: (Dez - Jan), funciona no ano mais Recente*/
    public static void limpezaAnoInicial(File diretorio, int mes, int dia, int hora, int horaTermino){
       File[] subdirsMes = diretorio.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);      
       for(File subsM : subdirsMes){
       String subMes = subsM.getName();
       int sMes = verificaMesDasPastas(subMes);
       if(horaTermino > hora && sMes == mes){
       File dias = new File(diretorio + "\\" + subMes);
       File [] subDias = dias.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);
      
       for(File diasSub : subDias){
       int diaS = Integer.parseInt(diasSub.getName().toString());
        
       if(diaS < dia){
           DeletarDiretorios(diasSub);
       }else if(horaTermino > hora && diaS==dia){
       File horas = new File(dias + "\\" + dia);
       File [] subHoras = horas.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);
         for (File horaSub : subHoras){
         int horaS = Integer.parseInt(horaSub.getName());
          if(horaS < hora){
              DeletarDiretorios(horaSub);
          }
         }
       }
       
       }
       }    
       }
        
   }

    //Essa Função verifica o Mes do diretorio e retorna o valor do mesmo
    public static  int verificaMesDasPastas(String dirName){
            int myDir = 0;
            switch(dirName){                
                case "Janeiro"  : 
                    myDir = 1;
                    break;
                case "Fevereiro":
                    myDir = 2;
                    break;
                case "Março":
                    myDir = 3;
                    break;
                case "Abril":
                    myDir = 4;
                    break;   
                case "Maio":
                    myDir = 5;
                    break;
                case "Junho":
                    myDir = 6;
                    break;
                case "Julho":
                    myDir = 7;
                    break;
                case "Agosto":
                    myDir = 8;
                    break;
                case "Setembro":
                    myDir = 9;
                    break;
                case "Outubro":
                    myDir = 10;
                    break;
                case "Novembro":
                    myDir = 11;
                    break;
                case "Dezembro":
                    myDir = 12;
                    break; 
                case "Jan"  : 
                    myDir = 1;
                    break;
                case "Fev":
                    myDir = 2;
                    break;
                case "Mar":
                    myDir = 3;
                    break;
                case "Abr":
                    myDir = 4;
                    break;   
                case "Mai":
                    myDir = 5;
                    break;
                case "Jun":
                    myDir = 6;
                    break;
                case "Jul":
                    myDir = 7;
                    break;
                case "Ago":
                    myDir = 8;
                    break;
                case "Set":
                    myDir = 9;
                    break;
                case "Out":
                    myDir = 10;
                    break;
                case "Nov":
                    myDir = 11;
                    break;
                case "Dez":
                    myDir = 12;
                    break;
                case "1":
                    myDir = 1;
                    break;
                case "2":
                    myDir = 2;
                    break;
                case "3":
                    myDir = 3;
                    break;
                case "4":
                    myDir = 4;
                    break;
                case "5":
                    myDir = 5;
                    break;
                case "6":
                    myDir = 6;
                    break;
                case "7":
                    myDir = 7;
                    break;
                case "8":
                    myDir = 8;
                   break;
                case "9":
                    myDir = 9;
                    break;
                case "10":
                    myDir = 10;
                    break;
                case "11":
                    myDir = 11;
                    break;
                case "12":
                    myDir = 12;
                    break;                  
            }
            return myDir;
                    }
  
    public static void DeletarDiretorios(File diretorio){
    try{
            FileUtils.deleteDirectory(diretorio);
           }catch(IOException e){
            e.printStackTrace();
            }    
     }
    
}
