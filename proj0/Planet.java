import java.lang.Math.*;
public class Planet{
    private static final double G = 6.67e-11;
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;


    public Planet(double xP, double yP, double xV, double yV, double m, String img){
        this.xxPos=xP;
        this.yyPos=yP;
        this.xxVel=xV;
        this.yyVel=yV;
        this.mass=m;
        this.imgFileName=img;
    }

    public Planet(Planet p){
        this(p.xxPos,p.yyPos,p.xxVel,p.yyVel,p.mass,p.imgFileName);
    }

    public double calcDistance(Planet p){
        if(p==null){
            return 0;
        }
        return Math.sqrt((this.xxPos-p.xxPos)*(this.xxPos-p.xxPos)+(this.yyPos-p.yyPos)*(this.yyPos-p.yyPos));
    }

    public double calcForceExertedBy(Planet p){
        if(p==null){
            return 0;
        }
        return (this.G*this.mass*p.mass)/(this.calcDistance(p)*this.calcDistance(p));
    }

    public double calcForceExertedByX(Planet p){
        if(p==null){
            return 0;
        }
        return (this.calcForceExertedBy(p)*(p.xxPos-this.xxPos))/this.calcDistance(p);
    }

    public double calcForceExertedByY(Planet p){
        if(p==null){
            return 0;
        }
        return (this.calcForceExertedBy(p)*(p.yyPos-this.yyPos))/this.calcDistance(p);
    }

    public double calcNetForceExertedByX(Planet[] p){
        double net = 0;
        if(p==null){
            return 0;
        }
        int i=0;
        while(i<p.length){
            if(!this.equals(p[i])){
                net = net + this.calcForceExertedByX(p[i]);
            }
            i=i+1;
        } 
        return net;
    }

    public double calcNetForceExertedByY(Planet[] p){
        double net = 0;
        if(p==null){
            return 0;
        }
        int i=0;
        while(i<p.length){
            if(!this.equals(p[i])){
                net = net + this.calcForceExertedByY(p[i]);
            }
            i=i+1;
        } 
        return net;
    }

    public void update(double time, double xn, double yn){
        this.xxVel =this.xxVel+ xn/this.mass*time;
        this.yyVel =this.yyVel+ yn/this.mass*time;

        this.xxPos=this.xxPos+this.xxVel*time;
        this.yyPos=this.yyPos+this.yyVel*time;
        
    }

    public void draw(){
		StdDraw.picture(this.xxPos, this.yyPos, this.imgFileName);

    }

}