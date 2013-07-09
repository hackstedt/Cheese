import simplegui
import random
import math

# initialize globals - pos and vel encode vertical info for paddles
WIDTH = 600
HEIGHT = 400       
BALL_RADIUS = 5

def normalize(v):
    # assert v[0] ==0 or v[1] ==0
    l = abs(v[0]+v[1])
    return (v[0]//l,v[1]//l)

def vector(p1,p2):
    """ vector from p1 and p2"""
    return (p2[0]-p1[0],p2[1]-p1[1])

def kreuzprodukt(v1,v2):
    return v1[0]*v2[1] - v1[1]*v2[0]

def makeSmaller(points_list):
    ret = []
    m = len(points_list)
    for i in range(m):
        left = points_list[(i-1) % m]
        right = points_list[(i+1) % m]
        mitte = points_list[i]
        mitteToLeft = vector(mitte,left)
        mitteToLeft = normalize(mitteToLeft)
        mitteToRight = vector(mitte,right)
        mitteToRight = normalize(mitteToRight)
        if(kreuzprodukt(mitteToLeft,mitteToRight) > 0):
            # a convex point
            ret.append((mitte[0]+mitteToLeft[0]+mitteToRight[0],
                    mitte[1]+mitteToLeft[1]+mitteToRight[1]))
        else:
            ret.append((mitte[0]-mitteToLeft[0]-mitteToRight[0],
                    mitte[1]-mitteToLeft[1]-mitteToRight[1]))
        
    return ret

def intersectionPoint(g1,g2):
    # using this formulas: http://community.topcoder.com/tc?module=Static&d1=tutorials&d2=geometry2
    def geradenParameter(p1,p2):
        x1=p1[0]
        x2=p2[0]
        y1=p1[1]
        y2=p2[1]
        A = y2-y1
        B = x1-x2
        C = A*x1+B*y1
        return (A,B,C)
    def onLineSegment(g,p):
        minx = min (g[0][0],g[1][0])
        maxx = max (g[0][0],g[1][0])
        miny = min (g[0][1],g[1][1])
        maxy = max (g[0][1],g[1][1])
        return (minx <=p[0] and p[0] <= maxx and miny <= p[1] and p[1] <= maxy)
    (A1,B1,C1) = geradenParameter(g1[0],g1[1])
    (A2,B2,C2) = geradenParameter(g2[0],g2[1])
    det = A1*B2 - A2*B1
    if(det < 0.01): # lines are parallel
        return None
    else:
        x = (B2*C1 - B1*C2)/det
        y = (A1*C2 - A2*C1)/det
        if(onLineSegment(g1,(x,y)) and onLineSegment(g2,(x,y))):
           return (x,y)
        else:
            return None
        

class Player:
    def __init__(self,pos,vel):
        self.col_point = None
        self.pos = pos
        self.vel = vel
        self.cheese = None # reference to current cheese
        self.streckenzug = []
        self.inside = False
        self.selfCollision = False
    def calcCollisionPoint(self,ls):
        """ ls = [ (gerade,Object)]  """
        col_point=None
        dist=100000
        for (toCheck,o) in ls:
                if(horizontal(toCheck[0],toCheck[1])):
                    y=toCheck[0][1]
                    xlinks=min(toCheck[0][0],toCheck[1][0])
                    xrechts=max(toCheck[0][0],toCheck[1][0])
                    if(not vel_horizontal(self.vel)):
                        if(xlinks < self.pos[0] and self.pos[0] < xrechts and (y-self.pos[1])*self.vel[1]>0):
                           if(abs((y-self.pos[1]))<dist):
                                dist = abs(y-self.pos[1]) 
                                col_point =((self.pos[0],y),o)
                else:
                    x= toCheck[0][0]
                    yoben = min(toCheck[0][1],toCheck[1][1])
                    yunten =max(toCheck[0][1],toCheck[1][1])
                    if(vel_horizontal(self.vel)):
                        if(yoben < self.pos[1] and self.pos[1] < yunten and (x-self.pos[0])*self.vel[0]>0):                        
                            if(abs(x-self.pos[0])<dist):
                                dist = abs(x-self.pos[0]) 
                                col_point =((x,self.pos[1]),o)
        return col_point
        
    def update(self,cheeses):
        possibleColision =[]
        for c in  cheeses:
            m= len(c.points)
            for i in range(m):
                possibleColision.append(([c.points[i],c.points[(i+1)%m]],c))
        if(self.inside):
            m= len(self.streckenzug)
            for i in range(m-1):
                possibleColision.append(([self.streckenzug[i],self.streckenzug[(i+1)]],"Self"))
            possibleColision.append(([self.streckenzug[-1],self.pos],"Self"))
            
        
        self.col_point = self.calcCollisionPoint(possibleColision)
        self.pos[0]+=self.vel[0]
        self.pos[1]+=self.vel[1]
        if(self.col_point and self.col_point[1] == "Self" and pointsEqual(self.col_point[0],self.pos) ):
            self.selfCollision = True
        if(self.col_point and type(self.col_point[1]) == Cheese ):
            if(pointsEqual(self.col_point[0],self.pos)):
                self.streckenzug.append(list(self.col_point[0]))
                if(not self.inside):
                    self.inside = True
                else:
                    # Logic to divide the cheese
                    self.inside = False
                    (c1,c2) = self.col_point[1].seperateBoth(self.streckenzug)
                    c1.points = makeSmaller(c1.points)
                    c2.points = makeSmaller(c2.points)
                    cheeses.remove(self.col_point[1])
                    if(c1.calcArea()>c2.calcArea()):
                        print c1.calcArea() , ">", c2.calcArea()
                        if(c2.hasFly()):
                            cheeses.append(c2)
                            if(c1.hasFly()):
                                cheeses.append(c1)
                        else:
                            cheeses.append(c1)
                    else:
                        print c1.calcArea() , "<", c2.calcArea()
                        if(c1.hasFly()):
                            cheeses.append(c1)
                            if(c2.hasFly()):
                                cheeses.append(c2)
                        else:
                            cheeses.append(c2)
                    self.streckenzug = []
        return cheeses
    def changeDirection(self,command):
        global ball_vel,ball_pos,col_point,inside,streckenzug
        if(command =="up"
            and not(self.vel[0]==0 and self.vel[1]==1)):
            self.vel[0]=0
            self.vel[1]=-1 
        if(command == "down"
            and not(self.vel[0]==0 and self.vel[1]==-1)):
            self.vel[0]=0
            self.vel[1]=1
        if(command == "right"
            and not(self.vel[0]==-1 and self.vel[1]==0)):
            self.vel[0]=1
            self.vel[1]=0
        if(command == "left"
            and not(self.vel[0]==1 and self.vel[1]==0)):
            self.vel[0]=-1
            self.vel[1]=0
        if (self.inside):
            self.streckenzug.append(tuple(self.pos))
    def draw(self,canvas):
        canvas.draw_circle(self.pos,BALL_RADIUS,1,"Red","Red")
        if self.col_point:
            canvas.draw_circle(self.col_point[0],BALL_RADIUS,1,"Green","Green")
        if(len(self.streckenzug)>0):
            for i in range(len(self.streckenzug)-1):
                canvas.draw_line(self.streckenzug[i], self.streckenzug[i+1], 
                    2, "Red")
            canvas.draw_line(self.streckenzug[len(self.streckenzug)-1], self.pos, 2, "Red")
        


                
def horizontal(p1,p2):
    """ do the two points form a horzontal line"""
    return p1[1]==p2[1]

class Flie:
    def __init__(self,pos,vel):
        self.pos = pos
        self.vel = vel
        self.collision = False
    def update(self,reflectors,collisons):
        """ reflectors are a list of distance, which reflect
        collision is the steckenzug of the plyer """
        nextpos=[self.pos[0] + self.vel[0],self.pos[1]+self.vel[1]]
        for g in collisons:
            x = intersectionPoint(g,(self.pos,nextpos))            
            if(x):
                self.collision = True
        m=len(reflectors)
        for g in reflectors:
            x = intersectionPoint(g,(self.pos,nextpos))            
            if(x):
                if(horizontal(g[0],g[1])):
                   self.vel[1]*=-1
                else:
                    self.vel[0]*=-1
                break
        nextpos=[self.pos[0] + self.vel[0],self.pos[1]+self.vel[1]]               
        self.pos = nextpos
    def draw(self,c):
        c.draw_circle(self.pos,BALL_RADIUS,1,"Black","Black")
    

def pointsEqual(p1,p2):
    return p1[0]==p2[0] and p1[1]==p2[1]

def horizontal(p1,p2):
    """ do the two points form a horzontal line"""
    return p1[1]==p2[1]

def vel_horizontal(v):
    return (abs(v[0])>0 and v[1]==0)

def dist(p1,p2):
    return math.sqrt((p1[0]-p2[0])**2 + (p1[1]-p2[1])**2)

def schneiden(g1,g2):
    if(horizontal(g1[0],g1[1]) and not horizontal(g2[0],g2[1]) ):
        schnittpunkt = (g2[0][0],g1[0][1])
        if (schnittpunkt[0]>min(g1[0][0],g1[1][0]) and schnittpunkt[0]<max(g1[0][0],g1[1][0]) and schnittpunkt[1]<max(g2[0][1],g2[1][1]) and schnittpunkt[1]<max(g2[0][1],g2[1][1]) ):
            return schnittpunkt
        else: return None
        
def calcGerade(pos,vel):
    if(vel[1]==1):
        return [pos,(pos[0],HEIGHT)]
    if(vel[1]==-1):
        return [pos,(pos[0],0)]
    if(vel[0]==1):
        return [pos,(WIDTH,pos[1])]
    if(vel[0]==-1): 
        return [pos,(0,pos[1])]

def selfCollisionPoint(pos,vel,streckenzug):
    # construct eigene Gerade
    gerade =calcGerade(pos,vel)
    m =len(streckenzug)
    cand = []
    for i in range(m-1):
        x = intersectionPoint(gerade,[streckenzug[i],streckenzug[i+1]] )
        if(x):
            cand.append(x)
     # finde closest
    mindist=1000
    ret = None
    if (len(cand)==0):
       return None
    else:
       for x in cand:
           if dist(pos,x)<mindist:
               ret = x
               mindist = dist(pos,x)
       return ret
            

class Cheese:
    def hasFly(self):
        return False
    def collisionPoint(self,pos,vel):
        """returns the next collision point"""
        col_point=None
        dist=100000
        m=len(self.points)
        for i in range(m):
            toCheck=(self.points[i],self.points[(i+1)%m])
            if(horizontal(toCheck[0],toCheck[1])):
                y=toCheck[0][1]
                xlinks=min(toCheck[0][0],toCheck[1][0])
                xrechts=max(toCheck[0][0],toCheck[1][0])
                if(not vel_horizontal(vel)):
                    if(xlinks < pos[0] and pos[0] < xrechts and (y-pos[1])*vel[1]>0):
                        if(abs((y-pos[1]))<dist):
                            dist = abs(y-pos[1]) 
                            col_point =(pos[0],y)
            else:
                x= toCheck[0][0]
                yoben = min(toCheck[0][1],toCheck[1][1])
                yunten =max(toCheck[0][1],toCheck[1][1])
                if(vel_horizontal(vel)):
                    if(yoben < pos[1] and pos[1] < yunten and (x-pos[0])*vel[0]>0):                        
                        if(abs(x-pos[0])<dist):
                            dist = abs(x-pos[0]) 
                            col_point =(x,pos[1])
        return col_point
                   
    def __init__(self,p1,p2,liste=None):
        """ will be initialised by an upper left point and 
        an lower right point"""
        if(liste):
           self.points = liste
        else:
            self.points = [[p1[0],p1[1]],
                          [p1[0],p2[1]],
                          [p2[0],p2[1]],
                          [p2[0],p1[1]]]
    def draw(self,canvas):
        canvas.draw_polygon(self.points, 1, "Green", "White")
    def findindex(self,point):
        m=len(self.points)
        for i in range(m):
            toCheck=(self.points[i],self.points[(i+1)%m])
            if(toCheck[0][0]==point[0] and toCheck[1][0] == point[0]): # alle haben Gleiche x Koordinaten und leigen auf einer vertiaklen Linie
                yoben = min(toCheck[0][1],toCheck[1][1])
                yunten =max(toCheck[0][1],toCheck[1][1])
                if(yoben < point[1] and point[1] < yunten):
                    return (i+1)%m
            if(toCheck[0][1]==point[1] and toCheck[0][1] == point[1]): # alle haben Gleiche y Koordinaten und leigen auf einer horizontalen Linie
                xlinks=min(toCheck[0][0],toCheck[1][0])
                xrechts=max(toCheck[0][0],toCheck[1][0])
                if(xlinks < point[0] and point[0] < xrechts):
                    return (i+1)%m
    def findIndexBefore(self,point):
        self.points=self.points[::-1]
        ret = self.findindex(point)
        self.points=self.points[::-1]
        return ret
    def seperate(self,streckenzug):
        # search the index of the point which follows
        # the last point of streckenzug
        startindex = self.findindex(streckenzug[0])
        endindex =self.findindex(streckenzug[-1])
        m=len(self.points)
        mystreckenzug = []
        for x in streckenzug:
            mystreckenzug.append(tuple(x))
        while(endindex!=startindex):
            mystreckenzug.append(tuple(self.points[endindex])) 
            endindex =  (endindex+1) % m
        #mystreckenzug.append(tuple(self.points[startindex]))            
        return Cheese(None,None,mystreckenzug)
    
    def seperateBoth(self,streckenzug):
        startindex = self.findindex(streckenzug[0])
        endindex =self.findindex(streckenzug[-1])
        if(startindex==endindex):
            print "Hi"
            m=len(self.points)
            mystreckenzug = []
            for x in streckenzug:
                mystreckenzug.append(tuple(x))
            for i in range(m):
                mystreckenzug.append(tuple(self.points[endindex])) 
                endindex =  (endindex+1) % m          
            return(Cheese(None,None,mystreckenzug),(Cheese(None,None,streckenzug)))
        else:    
            return (self.seperate(streckenzug),self.seperate(streckenzug[::-1]))
    def calcArea(self):
        """calcs the are using the formual from here
        http://en.wikipedia.org/wiki/Polygon#Area_and_centroid
        """
        m=len(self.points)
        summe=0
        for i in range(m):
            summe+=self.points[i][0]*self.points[(i+1)%m][1] - self.points[(i+1)%m][0]*self.points[i][1]
        return abs(summe/2)
        

    
class Game:
    def __init__(self):
        self.level=0
        self.lives=3
        
        self.nextLevel()
    def nextLevel(self):
        self.level +=1
        self.cheeses = [Cheese([30,30],[590,390])]
        self.originalSize=self.cheeses[0].calcArea()
        self.flies = [Flie([200,200],[1,2]),Flie([200,200],[-1,2])]
        self.player = Player([300,400],[0,0])
    def restartLevel(self):
        self.lives -=1
        self.cheeses = [Cheese([30,30],[590,390])]
        self.flies = [Flie([200,200],[1,2]),Flie([200,200],[-1,2])]
        self.player = Player([300,400],[0,0])
    def update(self):
        assert(self.lives>=0)
        #self.cheeses = [Cheese([100,100],[200,200])]
        if(self.lives>0):
            cheeses = self.player.update(self.cheeses)
            # check if the player has self.collided
            if(self.player.selfCollision):
                self.restartLevel()
                return None
            self.percentage =sum([c.calcArea() for c in cheeses])/self.originalSize            
            self.cheeses = cheeses
            reflectors =[]
            for c in  self.cheeses:
                m= len(c.points)
                for i in range(m):
                    reflectors.append([c.points[i],c.points[(i+1)%m]])
            collisions =[]
            if(self.player.inside):
                m= len(self.player.streckenzug)
                for i in range(m-1):
                    collisions.append([self.player.streckenzug[i],self.player.streckenzug[(i+1)]])
                collisions.append([self.player.streckenzug[-1],self.player.pos])
            for f in self.flies:
                f.update(reflectors,collisions)
                if(f.collision):
                    self.restartLevel()    
            if(self.percentage<0.25):
                self.nextLevel()
                self.update()
    def draw(self,canvas):
        if(self.lives>0):
            for c in self.cheeses:
                c.draw(canvas)
            self.player.draw(canvas)
            for f in self.flies:
                f.draw(canvas)
            canvas.draw_text("Level " + str(self.level), [20,20],20, "Red")
            canvas.draw_text("Lives " + str(self.lives), [200,20],20, "Red")
            canvas.draw_text("Percentage " + str("%.2f" % self.percentage), [400,20],20, "Red")
        else:
            canvas.draw_text("Game over!", [50,100],100, "Red")
            canvas.draw_text("Press space to start again", [200,200],20, "Red")

def draw(c):
    global game
    game.update()
    game.draw(c)
    
       
def keydown(key):
    global game  
    for command in ["up","down","right","left"]:
        if(key==simplegui.KEY_MAP[command]):
           game.player.changeDirection(command)
    if(game.lives<=0 and key==simplegui.KEY_MAP["space"]):
        game=Game()
        
   

cheese = Cheese((100,100),(300,300))
"""cheese = Cheese(None,None,[(100,100),(100,200),(110,200),
                           (110,220),(120,220),(120,200),
                           (200,200),(200,100)])"""
flies = [Flie([200,200],[1,2]),Flie([200,200],[-1,2])]

game = Game()

originalSize = cheese.calcArea()

# create frame
frame = simplegui.create_frame("Pong", WIDTH, HEIGHT)
frame.set_draw_handler(draw)
frame.set_keydown_handler(keydown)

# start frame
frame.start()
