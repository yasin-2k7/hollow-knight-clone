package com.hollowknight.view.game.enemiesView;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hollowknight.model.enemies.AttackWave;

public class AttackWaveView {
    private float stateTime = 0f;
    private AttackWave model;

    private Texture a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q;
    private TextureRegion aa,bb,cc,dd,ee,ff,gg,hh,ii,jj,kk,ll,mm,nn,oo,pp,qq;

    private Animation<TextureRegion> animation;



    public AttackWaveView(AttackWave model) {
        this.model = model;
        a = new Texture("effects/attackWave/highlight_019.png");
         b = new Texture("effects/attackWave/highlight_020.png");
         c = new Texture("effects/attackWave/highlight_021.png");
         d = new Texture("effects/attackWave/highlight_022.png");
         e = new Texture("effects/attackWave/highlight_023.png");
         f = new Texture("effects/attackWave/highlight_024.png");
         g = new Texture("effects/attackWave/highlight_025.png");
         h = new Texture("effects/attackWave/highlight_026.png");
         i = new Texture("effects/attackWave/highlight_027.png");
         j = new Texture("effects/attackWave/highlight_028.png");
         k = new Texture("effects/attackWave/highlight_029.png");
         l = new Texture("effects/attackWave/highlight_030.png");
         m = new Texture("effects/attackWave/highlight_031.png");
         n = new Texture("effects/attackWave/highlight_032.png");
         o = new Texture("effects/attackWave/highlight_037.png");
         p = new Texture("effects/attackWave/highlight_039.png");
         q = new Texture("effects/attackWave/highlight_040.png");
         aa = new TextureRegion(a);
         bb = new TextureRegion(b);
         cc = new TextureRegion(c);
         dd = new TextureRegion(d);
         ee = new TextureRegion(e);
         ff = new TextureRegion(f);
         gg = new TextureRegion(g);
         hh = new TextureRegion(h);
         ii = new TextureRegion(i);
         jj = new TextureRegion(j);
         kk = new TextureRegion(k);
         ll = new TextureRegion(l);
         mm = new TextureRegion(m);
         nn = new TextureRegion(n);
         oo = new TextureRegion(o);
         pp = new TextureRegion(p);
         qq = new TextureRegion(q);

        animation = new Animation<TextureRegion>(0.05f,new TextureRegion[]{aa,bb,cc,dd,ee,ff,gg,hh,ii,jj,kk,ll,mm,nn,oo,pp,qq});
    }

    public void draw(SpriteBatch batch, float delta){
        stateTime += delta;
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
        if (model.getDirection()==1 && !currentFrame.isFlipX()){
            currentFrame.flip(true, false);
        }
        else if (model.getDirection() == -1 && currentFrame.isFlipX()){
            currentFrame.flip(true, false);
        }
        batch.draw(currentFrame, model.getBounds().x, model.getBounds().y, model.getBounds().width, model.getBounds().height);
    }

    public void dispose(){

    }
}
