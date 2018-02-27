package com.example.chains;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;

import android.view.Display;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

public class MainActivity extends SimpleBaseGameActivity implements IOnSceneTouchListener{

	Camera cam;	int W=1366,H=1024;	
	Scene sn;
	ITextureRegion seg,toolbx1,rope,bk,brush,rect,circle,circlesg,eraser,mouse,distj,revoj,tbx2,reset;	
	ITiledTextureRegion pp;
	int M=0,n=0,zz=0;
	newWorld world=new newWorld(35, new Vector2(0,10), false);
	Body rctbdy[]=new Body[5000];
	Body rctb,l;
	Body rgdbdy;
	Rectangle rcts;
	boolean fj=true,rr=false;
	Rectangle tbx;
	Vector2 cpos= new Vector2();
	int c=0,j=0;
	Sprite tbx1,g;
	Sprite tools[]=new Sprite[50];
	float segw=10f,segh=25f;
	FixtureDef fd=PhysicsFactory.createFixtureDef(10, .3f, 0.5f);
	Entity objlayer=new Entity(); 
	Entity tblayer=new Entity();
	Entity jntlayer=new Entity();
	MouseJoint Mj;
	Rectangle rcs; 
	Rectangle r;
	Line ln;
	int jmod=0;
	Random rnd=new Random();
	Body tempb;
	Vector2 tmppos;
	Color clr;
	Entity rj1; Body bj1;
	VertexBufferObjectManager vbo;
	IEntity tmmp,ie;
	Joint J;
	
	@SuppressWarnings("deprecation")
	@Override
	public EngineOptions onCreateEngineOptions() {Display display = getWindowManager().getDefaultDisplay(); 
		W = display.getWidth(); 
		H = display.getHeight(); 
		cam=new Camera(0, 0, W, H);
		EngineOptions EO=new EngineOptions(true,ScreenOrientation.LANDSCAPE_FIXED,new FillResolutionPolicy(),cam);
		return EO;
	}

	@Override
	protected void onCreateResources() {	
		BitmapTextureAtlas atlas = new BitmapTextureAtlas(this.getTextureManager(), 512, 512,TextureOptions.NEAREST);
		BitmapTextureAtlas tb = new BitmapTextureAtlas(this.getTextureManager(), 512, 512,TextureOptions.BILINEAR);
		BitmapTextureAtlas tc = new BitmapTextureAtlas(this.getTextureManager(), 512, 512,TextureOptions.BILINEAR);
		
		BuildableBitmapTextureAtlas bkk = new  BuildableBitmapTextureAtlas(this.getTextureManager(),32, 32,TextureOptions.REPEATING_BILINEAR);
		
		bk =BitmapTextureAtlasTextureRegionFactory.createFromAsset(bkk, this, "back.png");		
		seg =BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, this, "segment.png", 0, 0);
		circlesg =BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, this, "circle-seg.png",55,0);
		
		toolbx1 =BitmapTextureAtlasTextureRegionFactory.createFromAsset(tb, this, "toolbx.png",0, 0);
		rope =BitmapTextureAtlasTextureRegionFactory.createFromAsset(tb, this, "rope.png",255, 0);
		brush =BitmapTextureAtlasTextureRegionFactory.createFromAsset(tb, this, "brush.png",310, 0);
		rect =BitmapTextureAtlasTextureRegionFactory.createFromAsset(tb, this, "rect.png",395, 0);
		circle =BitmapTextureAtlasTextureRegionFactory.createFromAsset(tb, this, "circle.png",0, 130);
		eraser =BitmapTextureAtlasTextureRegionFactory.createFromAsset(tb, this, "eraser.png",0, 200);
		mouse =BitmapTextureAtlasTextureRegionFactory.createFromAsset(tb, this, "mouse.png",0, 250);	
		reset =BitmapTextureAtlasTextureRegionFactory.createFromAsset(tb, this, "reset.png",255,100);
		
		pp =BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(tc, this, "playpause.png",0, 0,2,1);
		distj =BitmapTextureAtlasTextureRegionFactory.createFromAsset(tc, this, "dj.png",40, 86);
		revoj =BitmapTextureAtlasTextureRegionFactory.createFromAsset(tc, this, "rj.png",0, 86);
		tbx2 =BitmapTextureAtlasTextureRegionFactory.createFromAsset(tc, this, "tbx1.png",110, 86);
		
		try {
			bkk.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
			bkk.load();
			} catch (TextureAtlasBuilderException e) {
			Debug.e(e);
		}
		atlas.load();
		tb.load();
		tc.load();
		bkk.load();
	}
	
	@Override
	protected Scene onCreateScene() {
		vbo=getVertexBufferObjectManager();
		FixtureDef fn=PhysicsFactory.createFixtureDef(1f, 1f, 1f, true, (short)2, (short) 0,(short) 3);
		l=PhysicsFactory.createBoxBody(world, new Rectangle(0,0,0f,0,getVertexBufferObjectManager()), BodyType.StaticBody, fn);
		float D=(float) Math.sqrt(W*W+H*H);
		segh=D/30;
		segw=segh*0.4f;
		sn=new Scene();	
		sn.setOnSceneTouchListener(this);
		sn.registerUpdateHandler(world);		
		sn.setBackground(new Background(.95f,.95f,1f));
		
		bk.setTextureSize(400,400);

		Sprite back=new Sprite(0,0,W,H,bk,getVertexBufferObjectManager());
		Rectangle www=new Rectangle(0, 300, 234, 100, getVertexBufferObjectManager());
		www.setColor(234, 0, 0);
		sn.attachChild(back);
		sn.attachChild(jntlayer);
		sn.attachChild(objlayer);
		sn.attachChild(tblayer);
		//boundaries		
		Body f;
		Rectangle wall=new Rectangle(0, H-1 , W, 3, getVertexBufferObjectManager());
		wall.setColor(new Color(.0f, 1, 0));
		sn.attachChild(wall);
		f=PhysicsFactory.createBoxBody(world, wall, BodyType.StaticBody, fd);
		world.registerPhysicsConnector(new PhysicsConnector(wall, f));
		inittb();
		mode(0);
		
		
		
		
		return sn;
		
	}
	void inittb(){
		//toolbox	
				final float rto=1f;
				W*=.9f;
				final float hrto=1.3f;
				H*=hrto;
				
				tbx=new Rectangle(-W/4-W/16, 0 , W/4+W/16, H, getVertexBufferObjectManager()){
					
				};
				tbx.setColor(new Color(0, 0, 0,0.4f));
				tblayer.attachChild(tbx);

				//brush-tool
				tools[0]=new Sprite(W/40, H/40 +H/4, W/15f, H/15,brush, getVertexBufferObjectManager()){
					@Override
					public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
						if(pSceneTouchEvent.isActionUp())mode(0);
						return !rr;
					}
				};
				tbx.attachChild(tools[0]);

				//rope-tool
				tools[1]=new Sprite(W/40+W/15+W/80, H/40+H/4 , W/15f, H/15,rope, getVertexBufferObjectManager()){
					@Override
					public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
						if(pSceneTouchEvent.isActionUp())mode(1);
						return !rr;
					}
				};
				tbx.attachChild(tools[1]);

				//rect-tool
				tools[2]=new Sprite(W/40+2*W/15+W/40, H/40 +H/4, W/15f, H/15,rect, getVertexBufferObjectManager()){
					@Override
					public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
						if(pSceneTouchEvent.isActionUp())mode(2);
						return !rr;
					}
				};
				tbx.attachChild(tools[2]);

				//circle-tool
				tools[3]=new Sprite(W/50, H/40+H/15+H/30+H/4, W/15f, H/15,circle, getVertexBufferObjectManager()){
					@Override
					public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
						if(pSceneTouchEvent.isActionUp())mode(3);
						return !rr;
					}
				};
				tbx.attachChild(tools[3]);

				//delete-tool
				tools[4]=new Sprite(W/40+W/15+W/80, H/40, W/15f, H/15,eraser, getVertexBufferObjectManager()){
					@Override
					public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
						if(pSceneTouchEvent.isActionUp())mode(4);						
						return !rr;
					}
				};
				tbx.attachChild(tools[4]);
				
				//mouse-tool
				tools[5]=new Sprite(W/40, H/40, W/15f, H/15,mouse, getVertexBufferObjectManager()){
					@Override
					public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
						if(pSceneTouchEvent.isActionUp())mode(5);
						return !rr;
					}
				};
				tbx.attachChild(tools[5]);
				

				//DistantJoint-tool
				tools[6]=new Sprite(W/40,H/40+H/15+H/30+H/2.5f,W/15f, H/15,distj, getVertexBufferObjectManager()){
					@Override
					public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
						if(pSceneTouchEvent.isActionUp())mode(6);
						return !rr;
					}
				};
				tbx.attachChild(tools[6]);
				
				//RevoluteJoint-tool
				tools[7]=new Sprite(W/40+W/15+W/80,H/40+H/15+H/30+H/2.5f, W/15f, H/15,revoj, getVertexBufferObjectManager()){
					@Override
					public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
						if(pSceneTouchEvent.isActionUp())mode(7);
						return !rr;
					}
				};
				tbx.attachChild(tools[7]);
				
				//push button
				H/=hrto;
				tbx1=new Sprite(W/4+W/16,  H/2-H/16 , H/10f, H/9f,toolbx1, getVertexBufferObjectManager()){
					@Override
					public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
						if(pSceneTouchEvent.isActionUp())
							W*=rto;
							{rtt();
							W/=rto;
						}
						if(M==4)return true;
						return false;
					}		
				};
				Sprite tbxx2=new Sprite(W/4+W/16,  H/2-H/16 , H/10f, H/9f,tbx2, getVertexBufferObjectManager());
				tbxx2.setColor(1, 1, 1, .7f);
				tbx.attachChild(tbxx2);
				H*=hrto;
				sn.registerTouchArea(tbx1);
				tbx.attachChild(tbx1);

				//reset-tool
				tools[8]=new Sprite(W/40+W/15+W/80, H*.65f, W/15f, H/15,reset, getVertexBufferObjectManager()){
					@Override
					public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
						if(pSceneTouchEvent.isActionUp()){
							while(objlayer.getChildCount()!=0){
								IEntity ie=objlayer.getChildByIndex(0);
								tool4(pSceneTouchEvent,ie);
							}
						}
						return !rr;
					}
				};
				tbx.attachChild(tools[8]);
				
				//Play-Pause button
				AnimatedSprite as=new AnimatedSprite(W/40,  H*.65f , W/15f, H/15f,pp, getVertexBufferObjectManager()){
					@Override
					public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
						if(pSceneTouchEvent.isActionDown())
						if(this.getCurrentTileIndex()==0){
							this.setCurrentTileIndex(1);
							world.pausephys();
						}
						else{
							this.setCurrentTileIndex(0);
							world.playphys();							
						}
						return  !rr;
					}		
				};
				sn.registerTouchArea(as);
				tbx.attachChild(as);

				W/=rto;
				H/=rto;

				for(int cc=0;cc<9;cc++)sn.registerTouchArea(tools[cc]);
	}
	void mode(int x){
			if(!rr){
			for(int cc=0;cc<8;cc++)tools[cc].setColor(.2f,.2f,.2f);
			tools[x].setColor(1, 1, 1);
			M=x;
			jmod=0;
			rr=false;
		}
	}
	
	void rtt(){		
		if(tbx1.getRotation()==0){
			RotationModifier rttModifier = new RotationModifier(.1f,0,180);
			tbx1.registerEntityModifier(rttModifier);
			MoveModifier moveModifier = new MoveModifier(.1f, -W/4-W/16, 0, 0,0);
			tbx.registerEntityModifier(moveModifier);
		}
		else{
			RotationModifier rttModifier = new RotationModifier(.1f,180,0);
			tbx1.registerEntityModifier(rttModifier);
			MoveModifier moveModifier = new MoveModifier(.1f, 0, -W/4-W/16,0,0 );
			tbx.registerEntityModifier(moveModifier);
		}
	}
	
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent te) {
		if(M==0)tool0(te);
		if(M==1)tool1(te);
		if(M==2)tool2(te);
		if(M==3)tool3(te);
		if(M==5){Entity ne=new Entity();ne.setTag(-25);tool5(te,ne);};
		if(M==6){Entity ne=new Entity();ne.setTag(-25);tool6(te,ne);};
		if(M==7){Entity ne=new Entity();ne.setTag(-25);tool7(te,ne);};
		
		return false;
	}
	
	public void dot(float x,float y){
		Rectangle wall=new Rectangle(x-2, y-2 ,4, 4, getVertexBufferObjectManager());
		wall.setColor(new Color(1f, 0, 0));
		sn.attachChild(wall);
	}
	public void dot(Vector2 vec){
		float x=vec.x;
		float y=vec.y;
		Rectangle wall=new Rectangle(x-2, y-2 ,4, 4, getVertexBufferObjectManager());
		wall.setColor(new Color(1f, 0, 0));
		sn.attachChild(wall);
	}
	
	//brush-tool
	void tool0(TouchEvent te){
		if(te.isActionDown()){
			cpos.x=te.getX();
			cpos.y=te.getY();
			c=0;
			rcs=new Rectangle(0, 0, 0, 0, getVertexBufferObjectManager()){		
			};
			sn.registerTouchArea(rcs);
			objlayer.attachChild(rcs);
			rgdbdy=PhysicsFactory.createBoxBody(world, new Rectangle(0, 0, 3, 8, getVertexBufferObjectManager()), BodyType.DynamicBody,PhysicsFactory.createFixtureDef(fd.friction, fd.restitution, fd.friction/4));
			rgdbdy.setActive(false);
			PhysicsConnector kk=new PhysicsConnector(rcs,rgdbdy);
			world.registerPhysicsConnector(kk);
			rgdbdy.destroyFixture(rgdbdy.getFixtureList().get(0));
			clr=new Color(rnd.nextFloat(),rnd.nextFloat(),rnd.nextFloat());
			rr=true;
		}
		
		if(te.isActionUp()){	
			if(rr){
				rgdbdy.setActive(true);	
				objdata dat=new objdata();
				dat.bodies=new  ArrayList<Body>();
				dat.bodies.add(rgdbdy);
				rcs.setUserData(dat);
				sn.registerTouchArea(rcs);
			}
			rr=false;
		}
		
		if(te.isActionMove()){
			Vector2 tpos=new Vector2(te.getX(),te.getY());
			Vector2 v2t=new Vector2(tpos).sub(cpos);
			Vector2 nwpos=new Vector2();
			float ang=v2t.angle();
			nwpos=new Vector2(cpos).add((new Vector2(v2t).nor().mul(segh)));
			
			if(v2t.len()>segh){
				
				//sprite-----------
				rcts=new Rectangle(cpos.x, cpos.y ,segh*1.12f ,segw, getVertexBufferObjectManager()){	
					public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
						if(M==4)tool4(pSceneTouchEvent, this);
						if(M==5)tool5(pSceneTouchEvent,this);
						if(M==6)tool6(pSceneTouchEvent,this);
						if(M==7)tool7(pSceneTouchEvent,this);
						if(M==4||M==5||M==6)return true;
						return false;					
					}
				};
				sn.registerTouchArea(rcts);
				rcts.setTag(-3);
				rcts.setColor(clr);
				rcts.setRotation(ang);
				Vector2 zrpos=new Vector2(rcts.convertLocalToSceneCoordinates(0, 0)[0],rcts.convertLocalToSceneCoordinates(0, 0)[1]);
				Vector2 dst=zrpos.sub(cpos);
				rcts.setPosition(cpos.x-dst.x, cpos.y-dst.y);
				zrpos=new Vector2(rcts.convertLocalToSceneCoordinates(0, 0)[0],rcts.convertLocalToSceneCoordinates(0, 0)[1]);
				Vector2 zrpos2=new Vector2(rcts.convertLocalToSceneCoordinates(0, segw)[0],rcts.convertLocalToSceneCoordinates(0,segw)[1]);
				zrpos.sub(zrpos2);
				rcts.setPosition(cpos.x-dst.x+zrpos.x/2, cpos.y-dst.y+zrpos.y/2);
	
				Vector2 p1=new Vector2(rcts.convertLocalToSceneCoordinates(0, 0)[0],rcts.convertLocalToSceneCoordinates(0,0)[1]);
				Vector2 p4=new Vector2(rcts.convertLocalToSceneCoordinates(0, segw)[0],rcts.convertLocalToSceneCoordinates(0,segw)[1]);
				Vector2 p3=new Vector2(rcts.convertLocalToSceneCoordinates(segh, segw)[0],rcts.convertLocalToSceneCoordinates(segh,segw)[1]);
				Vector2 p2=new Vector2(rcts.convertLocalToSceneCoordinates(segh,0 )[0],rcts.convertLocalToSceneCoordinates(segh,0)[1]);
				p1.div(PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
				p2.div(PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
				p3.div(PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
				p4.div(PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
				FixtureDef ffd=PhysicsFactory.createFixtureDef(fd.density,fd.restitution,fd.friction);
				final PolygonShape shp = new PolygonShape();
				Vector2[] vcs={p1,p2,p3,p4};
				shp.set(vcs);
				ffd.shape=shp;
				rgdbdy.createFixture(ffd);
				rcs.attachChild(rcts);
				cpos=new Vector2(nwpos);
				if(new Vector2(nwpos).sub(tpos).len()>segh){
					onSceneTouchEvent(sn, te);
				}
				
				
				
			}
			
		}
	}
	
	//rope-tool
	void tool1(TouchEvent te){
		float segh= this.segh*1.3f;
		float segw= this.segw*1.3f;
		if(te.isActionDown()){
			cpos.x=te.getX();
			cpos.y=te.getY();
			fj=true;
			rr=true;
			c=0;
			j++;
			clr=new Color(rnd.nextFloat()+.5f,rnd.nextFloat()+.5f,rnd.nextFloat()+.5f);
		}
		
		if(te.isActionUp()){
			for(int f=0;f<c;f++){
				rctbdy[f].setActive(true);
				rctbdy[f].setUserData(c);
			}
			rr=false;
		}
		
		if(te.isActionMove()){
			Vector2 tpos=new Vector2(te.getX(),te.getY());
			Vector2 v2t=new Vector2(tpos).sub(cpos);
			Vector2 nwpos=new Vector2();
			float ang=v2t.angle();
			nwpos=new Vector2(cpos).add((new Vector2(v2t).nor().mul(segh)));
			
			if(v2t.len()>segh){
				
				//sprite-----------
				Sprite wall=new Sprite(cpos.x, cpos.y ,segh ,segw,seg , getVertexBufferObjectManager()){	
					public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
						if(M==4)tool4(pSceneTouchEvent, this);
						if(M==5)tool5(pSceneTouchEvent,this);
						if(M==6)tool6(pSceneTouchEvent,this);
						if(M==7)tool7(pSceneTouchEvent,this);
						if(M==4||M==5||M==6)return true;
						return false;				
					}
				};
				sn.registerTouchArea(wall);
				wall.setColor(clr);
				wall.setRotation(ang);
				Vector2 zrpos=new Vector2(wall.convertLocalToSceneCoordinates(0, 0)[0],wall.convertLocalToSceneCoordinates(0, 0)[1]);
				Vector2 dst=zrpos.sub(cpos);
				wall.setPosition(cpos.x-dst.x, cpos.y-dst.y);
				zrpos=new Vector2(wall.convertLocalToSceneCoordinates(0, 0)[0],wall.convertLocalToSceneCoordinates(0, 0)[1]);
				Vector2 zrpos2=new Vector2(wall.convertLocalToSceneCoordinates(0, segw)[0],wall.convertLocalToSceneCoordinates(0,segw)[1]);
				zrpos.sub(zrpos2);
				wall.setPosition(cpos.x-dst.x+zrpos.x/2, cpos.y-dst.y+zrpos.y/2);
				objlayer.attachChild(wall);
				
				//body-----------
				rctbdy[c]=PhysicsFactory.createBoxBody(world, wall, BodyType.DynamicBody, fd);
				rctbdy[c].setActive(false);
				rctbdy[c].setAngularDamping(40f);
				rctbdy[c].setLinearDamping(0.5f);
				world.registerPhysicsConnector(new PhysicsConnector(wall, rctbdy[c]));
				wall.setUserData(rctbdy[c]);
				objdata dat=new objdata();
				dat.bodies=new  ArrayList<Body>();
				dat.bodies.add(rctbdy[c]);
				wall.setUserData(dat);
				
				//joint---------
				if(!fj){
					RevoluteJointDef rj = new RevoluteJointDef();
					rj.initialize(rctbdy[c-1], rctbdy[c],cpos.div(PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT));
					rj.collideConnected=false;
					world.createJoint(rj);
				}
				
				cpos=new Vector2(nwpos);
				fj=false;
				c++;
				if(new Vector2(nwpos).sub(tpos).len()>segh){
					onSceneTouchEvent(sn, te);
				}
				
			}
			
		}

	}
	
	//rectangle-tool
	void tool2(TouchEvent te){	
		if(te.isActionDown()){
			cpos.x=te.getX();
			cpos.y=te.getY();
			r=new Rectangle(cpos.x,cpos.y,0,0 , getVertexBufferObjectManager()){	
				public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
					if(M==4)tool4(pSceneTouchEvent,this);
					if(M==5)tool5(pSceneTouchEvent,this);
					if(M==6)tool6(pSceneTouchEvent,this);
					if(M==7)tool7(pSceneTouchEvent,this);
					if(M==4||M==5||M==6)return true;
					return false;					
				}
			};
			sn.registerTouchArea(r);
			r.setColor(rnd.nextFloat(), rnd.nextFloat(), rnd.nextFloat());
			objlayer.attachChild(r);
			rr=true;
		}
		if(te.isActionMove())if(rr==true){
				float x=te.getX();
				float y=te.getY();
				r.setWidth(x-cpos.x);
				r.setHeight(y-cpos.y);
		}
		if(te.isActionUp())if(rr==true){
				if((r.getHeight()!=0)&&(r.getWidth()!=0)){
					if(r.getHeight()<0){ r.setHeight(r.getHeight()*-1);r.setY(r.getY()-r.getHeight());}
					if(r.getWidth()<0){ r.setWidth(r.getWidth()*-1);r.setX(r.getX()-r.getWidth());}
					r.setRotationCenter(r.getWidth()/2, r.getHeight()/2);
					r.setSkewCenter(r.getWidth()/2, r.getHeight()/2);
					rctb=PhysicsFactory.createBoxBody(world,r, BodyType.DynamicBody, fd);
					world.registerPhysicsConnector(new PhysicsConnector(r,rctb));
					objdata dat=new objdata();
					dat.bodies=new  ArrayList<Body>();
					dat.bodies.add(rctb);
					r.setUserData(dat);
				}
				rr=false;
		}
		
	}	

	//circle-tool
	void tool3(TouchEvent te){

		if(te.isActionDown()){
			cpos.x=te.getX();
			cpos.y=te.getY();
			g=new Sprite(cpos.x,cpos.y,0,0 ,circlesg, getVertexBufferObjectManager()){	
				public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
					
					if(M==4)tool4(pSceneTouchEvent, this);
					if(M==5)tool5(pSceneTouchEvent,this);
					if(M==6)tool6(pSceneTouchEvent,this);
					if(M==7)tool7(pSceneTouchEvent,this);
					if(M==4||M==5||M==6)return true;
					return false;
				}
			};
			sn.registerTouchArea(g);
			g.setColor(rnd.nextFloat(), rnd.nextFloat(), rnd.nextFloat());
			objlayer.attachChild(g);
			
			rr=true;
		}
		if(te.isActionMove())if(rr==true){
				float x=te.getX();
				float y=te.getY();
				float R=new Vector2(cpos).dst(x, y);
				g.setX(cpos.x-R);
				g.setY(cpos.y-R);
				g.setWidth(R*2);
				g.setHeight(R*2);
		}
		if(te.isActionUp())if(rr==true){
				if((g.getHeight()!=0)&&(g.getWidth()!=0)){
					if(g.getHeight()<0){ g.setHeight(g.getHeight()*-1);g.setY(g.getY()-g.getHeight());}
					if(g.getWidth()<0){ g.setWidth(g.getWidth()*-1);g.setX(g.getX()-g.getWidth());}
					g.setRotationCenter(g.getWidth()/2, g.getHeight()/2);
					g.setSkewCenter(g.getWidth()/2, g.getHeight()/2);
					rctb=PhysicsFactory.createCircleBody(world,g, BodyType.DynamicBody, fd);
					world.registerPhysicsConnector(new PhysicsConnector(g,rctb));
					objdata dat=new objdata();
					dat.bodies=new  ArrayList<Body>();
					dat.bodies.add(rctb);
					g.setUserData(dat);
				}
				rr=false;
		}
		
	}
	
	//delete
	void tool4(TouchEvent te,IEntity tob){	
		if(te.isActionUp()){
			if(tob.getTag()==-3)tob=tob.getParent();
			objdata oj=(objdata) tob.getUserData();
			List<Body> bod=oj.bodies;
			List<Entity> ents=oj.objs;
			for(int kh=0;kh<bod.size();kh++){
				Body pqr= bod.get(kh);
				if(pqr.isActive())
				world.destroyBody(pqr);
				pqr.setActive(false);
			}
			for(int kh=0;kh<ents.size();kh++){
				Entity pqr= ents.get(kh);
				pqr.detachSelf();
			}
			if(tob.getParent()!=null){
				tob.detachSelf();

				tob.dispose();
			}
			Object h=objlayer;
			sn.unregisterTouchArea((ITouchArea) tob);
		}
		
	}
	
	//mouse drag
	void tool5(TouchEvent te,IEntity tob){
		if(te.isActionDown()){
			if(tob.getTag()==-25)return;
			if(tob.getTag()==-3)tob=tob.getParent();
			if(!rr){
				MouseJointDef mj=new MouseJointDef();
				mj.target.set(te.getX()/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,te.getY()/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
				mj.bodyA=l;
				objdata oj=(objdata) tob.getUserData();				
				mj.bodyB=oj.bodies.get(0);				
				mj.maxForce=100000*mj.bodyB.getMass();
				if(mj.bodyB.getUserData() != null)		
				mj.maxForce=100000*Float.valueOf(mj.bodyB.getUserData().toString());
				mj.dampingRatio=0;
				mj.frequencyHz=10f;				
				mj.collideConnected=false;
				Mj=(MouseJoint) world.createJoint(mj);
				rr=true;
			}
		}
		if(te.isActionMove()){
			if(rr){
				Vector2 tu=new Vector2(te.getX()/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,te.getY()/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
				Mj.setTarget(tu);
			}
		}
		if(te.isActionCancel() ||	te.isActionOutside() ||te.isActionUp()) {
			if(rr){
				world.destroyJoint(Mj);
			}
			rr=false;
		}
	}
	
	//distant-joint
	void tool6(TouchEvent te,IEntity tob){
		if(te.isActionDown()){
			
			{
				//if(tob.getTag()==-25)if(jmod==0)return; 
				if(tob.getTag()==-3)tob=tob.getParent();
				if((tob.getTag()!=-25)&&(tob.getUserData()!=null)){
					objdata oj=(objdata) tob.getUserData();
					tempb=oj.bodies.get(0);
					tmmp=tob;
				}
				else tempb=l;
				 ie=tob;
				tmppos=new Vector2(te.getX(),te.getY());
				tmppos.div(PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
				jmod=1;
				ln=new Line(0,0,0,0,vbo){
					@Override
					public void onManagedUpdate(final float pSecondsElapsed){
						if(M!=6)this.detachSelf();
					}
				};
				ln.setLineWidth(3);
				ln.setColor(1, 1, 0);
				objlayer.attachChild(ln);
			}
		}
		if(te.isActionMove()){
			if(jmod==1)
			ln.setPosition(new Vector2(tmppos).mul(PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT).x,new Vector2(tmppos).mul(PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT).y,new Vector2(te.getX(),te.getY()).x,new Vector2(te.getX(),te.getY()).y);
				
		}
		if(te.isActionUp()) {
			if(jmod==1)
			{

				if(tob.getTag()==-3)tob=tob.getParent();
				final DistanceJointDef mj=new DistanceJointDef();
				Body tempbb;
				if((tob.getTag()!=-25)&&(tob.getUserData()!=null)){
					objdata oj=(objdata) tob.getUserData();
					tempbb=oj.bodies.get(0);
				}
				else tempbb=l;
				
				mj.collideConnected=true;
				mj.frequencyHz=0;
				mj.initialize(tempb, tempbb,tmppos, (new Vector2(te.getX(),te.getY())).div(PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT));
				Vector2 A,B;
				A=(new Vector2( mj.localAnchorA).add(mj.bodyA.getPosition()).mul(PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT));
				B=(new Vector2( mj.localAnchorB).add(mj.bodyB.getPosition()).mul(PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT));		
				if(mj.bodyA!=mj.bodyB)
					J=world.createJoint(mj);		
				Rectangle djl=new Rectangle(0,0,B.x,3,getVertexBufferObjectManager()){
					@Override
					public void onManagedUpdate(final float pSecondsElapsed){
						Vector2 A,B;
						A= mj.bodyA.getWorldPoint(mj.localAnchorA).mul(PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
						B= mj.bodyB.getWorldPoint(mj.localAnchorB).mul(PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
						this.setWidth(new Vector2(A).dst(B));
						this.setPosition(A.x,A.y);
						this.setRotationCenter(0,0);
						this.setRotation(new Vector2(B).sub(A).angle());
					}
					@Override
					public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
						if(pSceneTouchEvent.isActionUp())if(M==4){
							world.destroyJoint((Joint)this.getUserData());
							this.detachSelf();
							sn.unregisterTouchArea(this);							
							return true;
						}
						return true;
					}					
				};
				djl.setUserData(J);
				sn.getTouchAreas().add(0, djl);
				objlayer.attachChild(djl);
				objlayer.detachChild(ln);
				if(mj.bodyA!=mj.bodyB){
					//J=world.createJoint(mj);
					if(tmmp!=null){
						objdata bb=(objdata)tmmp.getUserData();
						bb.objs.add(djl);
					}
					if(tob!=null){
						objdata bb=(objdata)tob.getUserData();
						if(bb!=null)
						bb.objs.add(djl);
					}
				}
				else djl.detachSelf();
				
				jmod=0;
			}
		}
	}


	//revolute-joint
	void tool7(TouchEvent te,IEntity tob){
		if(te.isActionUp()) {
			if(tob.getUserData()==null)if(tob.getTag()!=-25)return;
			if(tob.getTag()==-3)tob=tob.getParent();
			
			if(jmod==0){
				if(tob.getTag()==-25)return;
				rj1=(Entity) tob;
				jmod=1;
			}
			else if(jmod==1){

				final RevoluteJointDef mj=new RevoluteJointDef();
				Body tempbb;
				if(tob.getTag()!=-25){
					objdata oj=(objdata) tob.getUserData();
					tempbb=oj.bodies.get(0);
				}
				else tempbb=l;
				

				objdata oj=(objdata) rj1.getUserData();
				Body rjb=oj.bodies.get(0);
				
				mj.collideConnected=false;
				mj.initialize(rjb, tempbb,(new Vector2(te.getX(),te.getY())).div(PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT));
				final Joint J=world.createJoint(mj);
				final float s=20;
				Sprite djl=new Sprite(0,0,s,s,revoj,getVertexBufferObjectManager()){
					@Override
					public void onManagedUpdate(final float pSecondsElapsed){
						Vector2 A;
						A= mj.bodyA.getWorldPoint(mj.localAnchorA).mul(PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
						this.setPosition(A.x-s/2, A.y-s/2);
					}
					@Override
					public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
						if(pSceneTouchEvent.isActionUp())if(M==4){
							world.destroyJoint(J);
							this.detachSelf();
							sn.unregisterTouchArea(this);
							return true;
						}
						return false;
					}
					
				};
				djl.setZIndex(-3);
				sn.getTouchAreas().add(0, djl);
				sn.attachChild(djl);
				

				objdata bb=(objdata)rj1.getUserData();
				bb.objs.add(djl);
				if(tob.getTag()!=-25){
					
					tob.setColor(tob.getRed(), tob.getGreen(), tob.getBlue(), .8f);
					
					bb=(objdata)tob.getUserData();
					bb.objs.add(djl);
				}
				
				jmod=0;
			}
			
		}
	}

}

