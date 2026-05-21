// PlayState.java - Fixed Version

package javaapplication9;

import com.jogamp.opengl.GL2;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class PlayState implements GameState {
    // Game objects
    private Player player;
    private ArrayList<Coin> coins;
    private ArrayList<Enemy> enemies;
    private ArrayList<Obstacle> obstacles;
    private ArrayList<PowerUp> powerUps;
    private ArrayList<Particle> particles;
    private ArrayList<Bullet> bullets;
    private ArrayList<FloatingText> floatingTexts;
    private ArrayList<ShootingEnemy> shootingEnemies;
    private ArrayList<EnemyBullet> enemyBullets;
    private ArrayList<FallingObstacle> fallingObstacles;
    private ArrayList<Boss> bosses;
    // في بداية PlayState.java
private static final double GROUND_TOP = 520;     // مكان الأرض من تحت
private static final double GROUND_Y = 520;       // ي轴的أرض
private static final double ENEMY_Y = 475;        // Y الأعداء (GROUND_Y - ارتفاع العدو)
private static final double BOSS_Y = 430;         // Y الزعيم
    // Game state
    private int score = 0;
    private int lives = 3;
    private int combo = 0;
    private int comboMultiplier = 1;
    private long lastKillTime = 0;
    private int weaponType = 0;
    private int attackCooldown = 0;
    private double gameSpeed = 5.0;
    private int spawnCounter = 0;
    private int level = 1;
    private int scoreForNextLevel = 500;
    private boolean bossSpawned = false;
    private int bossSpawnScore = 1000;
    private int fallingSpawnCounter = 0;
    private double bgOffset = 0;
    private double normalGameSpeed = 5.0;
    private boolean isRunningFast = false;
    
 
    
    private Random random;
    private int width, height;
    
    private boolean showInfoScreen = true;
    private double infoScreenTimer = 180;

    public PlayState(int width, int height) {
        this.width = width;
        this.height = height;
        this.random = new Random();
        initPlayState();
    }
    
    private void initPlayState() {
        // Player at ground level
         Sound.play("levelstart.wav");
        player = new Player(100, GROUND_Y);
        player.groundY = GROUND_Y;
       
        coins = new ArrayList<>();
        enemies = new ArrayList<>();
        obstacles = new ArrayList<>();
        powerUps = new ArrayList<>();
        particles = new ArrayList<>();
        bullets = new ArrayList<>();
        floatingTexts = new ArrayList<>();
        shootingEnemies = new ArrayList<>();
        enemyBullets = new ArrayList<>();
        fallingObstacles = new ArrayList<>();
        bosses = new ArrayList<>();
        
        gameSpeed = 5.0;
        normalGameSpeed = 5.0;
        score = 0;
        lives = 3;
        level = 1;
        combo = 0;
        weaponType = 0;
        bossSpawned = false;
        bossSpawnScore = 1000;
        fallingSpawnCounter = 0;
    }
    
    @Override
    public void update(Game game) {
        if (showInfoScreen) {
            updateInfoScreen();
        } else {
            updateGameplay();
            
            if (lives <= 0) {
                  Sound.play("over.wav");
                game.setHighScore(score);
                game.getStateManager().setState(new GameOverState());
            }
        }
    }

    public int getScore() { return score; }
    public int getLives() { return lives; }
    public int getLevel() { return level; }
    
    private void updateGameplay() {
        bgOffset += gameSpeed * 0.5;
        player.update();
        gameSpeed = 5 + (score / 500.0);
        if (gameSpeed > 12) gameSpeed = 12;
        
        updateSpeeds();
        updateCoins();
        updateEnemies();
        updateObstacles();
        updatePowerUps();
        updateBullets();
        updateParticles();
        updateBosses();
        updateFallingObstacles();
        updateShootingEnemies();
        updateEnemyBullets();
        updateFloatingTexts();
        
        spawnObjects();
        checkLevelUp();
        
        if (attackCooldown > 0) attackCooldown--;
    }
    
    private void updateSpeeds() {
        for (Coin coin : coins) coin.speed = gameSpeed;
        for (Obstacle obs : obstacles) obs.speed = gameSpeed;
        for (PowerUp power : powerUps) power.speed = gameSpeed;
    }
    
    private void updateCoins() {
        Iterator<Coin> it = coins.iterator();
        while (it.hasNext()) {
            Coin coin = it.next();
            coin.update();
            if (coin.isOffScreen()) {
                it.remove();
            } else if (checkCollision(player, coin.x, coin.y, coin.size, coin.size)) {
                score += 10;
                it.remove();
                createPickupParticles(coin.x + coin.size/2, coin.y + coin.size/2, 1, 0.84f, 0);
                Sound.play("coin.wav");
            }
        }
    }
    
    private void updateEnemies() {
        Iterator<Enemy> it = enemies.iterator();
        while (it.hasNext()) {
            Enemy enemy = it.next();
            enemy.update(player.x);
            if (enemy.isOffScreen()) {
                it.remove();
            } else if (checkCollision(player, enemy.x, enemy.y, enemy.width, enemy.height)) {
                if (!player.invincible) {
                    lives--;
                    it.remove();
                    createExplosionParticles(enemy.x + enemy.width/2, enemy.y + enemy.height/2);
                    player.setInvincible(1500);
                    floatingTexts.add(new FloatingText(player.x, player.y - 50, "-1 LIFE", 1, 0, 0));
                    Sound.play("hurt.wav");
                } else {
                    it.remove();
                    createExplosionParticles(enemy.x + enemy.width/2, enemy.y + enemy.height/2);
                }
            }
        }
    }
    
    private void updateObstacles() {
        Iterator<Obstacle> it = obstacles.iterator();
        while (it.hasNext()) {
            Obstacle obs = it.next();
            obs.update();
            if (obs.isOffScreen()) {
                it.remove();
            } else if (checkCollision(player, obs.x, obs.y, obs.width, obs.height)) {
                if (!obs.hasHit && !player.invincible) {
                    lives--;
                    obs.hasHit = true;
                    createExplosionParticles(obs.x + obs.width/2, obs.y + obs.height/2);
                    player.setInvincible(1500);
                    floatingTexts.add(new FloatingText(player.x, player.y - 50, "-1 LIFE", 1, 0, 0));
                    Sound.play("hurt.wav");
                }
            }
        }
    }
    
    private void updatePowerUps() {
        Iterator<PowerUp> it = powerUps.iterator();
        while (it.hasNext()) {
            PowerUp power = it.next();
            power.update();
            if (power.isOffScreen()) {
                it.remove();
            } else if (checkCollision(player, power.x, power.y, power.size, power.size)) {
                if (power.type == 0) {
                    score += 50;
                } else {
                    lives++;
                    if (lives > 5) lives = 5;
                }
                it.remove();
                createPickupParticles(power.x + power.size/2, power.y + power.size/2, 0.5f, 0.5f, 1);
                Sound.play("powerup.wav");
            }
        }
    }
    
    private void updateBullets() {
        Iterator<Bullet> it = bullets.iterator();
        while (it.hasNext()) {
            Bullet bullet = it.next();
            bullet.update();
            if (!bullet.active) {
                it.remove();
                continue;
            }
            
            boolean hit = false;
            
            // Check Boss
            Iterator<Boss> bossIt = bosses.iterator();
            while (bossIt.hasNext() && !hit) {
                Boss boss = bossIt.next();
                if (bullet.checkHit(boss)) {
                    boss.takeDamage();
                    it.remove();
                    hit = true;
                    if (!boss.active) {
                        bossIt.remove();
                        score += 200;
                        bossSpawned = false;
                        createExplosionParticles(boss.x + boss.width/2, boss.y + boss.height/2);
                        floatingTexts.add(new FloatingText(boss.x, boss.y, "BOSS DEFEATED! +200", 1, 0.8f, 0));
                    }
                }
            }
            
            // Check Shooting Enemies
            if (!hit) {
                Iterator<ShootingEnemy> shootIt = shootingEnemies.iterator();
                while (shootIt.hasNext() && !hit) {
                    ShootingEnemy senemy = shootIt.next();
                    if (bullet.x < senemy.x + senemy.width &&
                        bullet.x + bullet.size > senemy.x &&
                        bullet.y - bullet.size/2 < senemy.y + senemy.height &&
                        bullet.y + bullet.size/2 > senemy.y) {
                        senemy.takeDamage();
                        it.remove();
                        hit = true;
                        if (!senemy.active) {
                            shootIt.remove();
                            score += 40;
                            createExplosionParticles(senemy.x + senemy.width/2, senemy.y + senemy.height/2);
                            floatingTexts.add(new FloatingText(senemy.x, senemy.y, "+40", 1, 0.5f, 0));
                        }
                    }
                }
            }
            
            // Check Normal Enemies
            if (!hit) {
                Iterator<Enemy> enemyIt = enemies.iterator();
                while (enemyIt.hasNext() && !hit) {
                    Enemy enemy = enemyIt.next();
                    if (bullet.x < enemy.x + enemy.width &&
                        bullet.x + bullet.size > enemy.x &&
                        bullet.y - bullet.size/2 < enemy.y + enemy.height &&
                        bullet.y + bullet.size/2 > enemy.y) {
                        enemyIt.remove();
                        it.remove();
                        hit = true;
                        
                        long now = System.currentTimeMillis();
                        if (now - lastKillTime < 2000) {
                            combo++;
                            if (combo > 10) combo = 10;
                            comboMultiplier = 1 + (combo / 3);
                        } else {
                            combo = 1;
                            comboMultiplier = 1;
                        }
                        lastKillTime = now;
                        
                        int points = 30 * comboMultiplier;
                        score += points;
                        floatingTexts.add(new FloatingText(enemy.x, enemy.y, "+" + points, 1, 0.8f, 0));
                        createExplosionParticles(enemy.x + enemy.width/2, enemy.y + enemy.height/2);
                    }
                }
            }
        }
    }
    
    private void updateBosses() {
        Iterator<Boss> it = bosses.iterator();
        while (it.hasNext()) {
            Boss boss = it.next();
            boss.update(player.x, player.y);
            if (boss.isOffScreen()) {
                it.remove();
                bossSpawned = false;
            }
        }
        
        if (!bossSpawned && score >= bossSpawnScore) {
            bosses.add(new Boss(width, 250));
            bossSpawned = true;
            bossSpawnScore += 1000;
        }
    }
    
    private void updateFallingObstacles() {
        fallingSpawnCounter++;
        if (fallingSpawnCounter > 500) {
            fallingObstacles.add(new FallingObstacle(random.nextInt(width - 50) + 25));
            fallingSpawnCounter = 0;
        }
        
        Iterator<FallingObstacle> it = fallingObstacles.iterator();
        while (it.hasNext()) {
            FallingObstacle fall = it.next();
            fall.update();
            if (!fall.active) {
                it.remove();
            } else if (fall.checkCollision(player.x, player.y - player.height, player.width, player.height)) {
                if (!player.invincible) {
                    lives--;
                    it.remove();
                    createExplosionParticles(fall.x + fall.size/2, fall.y + fall.size/2);
                    player.setInvincible(1500);
                    floatingTexts.add(new FloatingText(player.x, player.y - 50, "-1 LIFE", 1, 0, 0));
                    Sound.play("hurt.wav");
                }
            }
        }
    }
    
    private void updateShootingEnemies() {
        Iterator<ShootingEnemy> it = shootingEnemies.iterator();
        while (it.hasNext()) {
            ShootingEnemy senemy = it.next();
            senemy.update(player.x, player.y - player.height/2);
            if (senemy.isOffScreen()) {
                it.remove();
            } else if (!senemy.active) {
                score += 40;
                createExplosionParticles(senemy.x + senemy.width/2, senemy.y + senemy.height/2);
                floatingTexts.add(new FloatingText(senemy.x, senemy.y, "+40", 1, 0.5f, 0));
                it.remove();
            }
        }
    }
    
    private void updateEnemyBullets() {
        Iterator<EnemyBullet> it = enemyBullets.iterator();
        while (it.hasNext()) {
            EnemyBullet eb = it.next();
            eb.update();
            if (!eb.active) {
                it.remove();
            } else if (checkCollision(player, eb.x - eb.size/2, eb.y - eb.size/2, eb.size, eb.size)) {
                if (!player.invincible) {
                    lives--;
                    it.remove();
                    player.setInvincible(1500);
                    floatingTexts.add(new FloatingText(player.x, player.y - 50, "-1 LIFE", 1, 0, 0));
                    Sound.play("hurt.wav");
                } else {
                    it.remove();
                }
            }
        }
    }
    
    private void updateParticles() {
        Iterator<Particle> it = particles.iterator();
        while (it.hasNext()) {
            Particle p = it.next();
            p.update();
            if (p.isDead()) it.remove();
        }
    }
    
    private void updateFloatingTexts() {
        Iterator<FloatingText> it = floatingTexts.iterator();
        while (it.hasNext()) {
            FloatingText ft = it.next();
            ft.update();
            if (ft.isDead()) it.remove();
        }
    }
    
    private void updateInfoScreen() {
        if (showInfoScreen) {
            infoScreenTimer--;
            if (infoScreenTimer <= 0) {
                showInfoScreen = false;
            }
        }
    }
    
  private void spawnObjects() {
    spawnCounter++;
    int spawnDelay = Math.max(25, 50 - (int)(score / 150));
    if (spawnCounter > spawnDelay) {
        int rand = random.nextInt(100);
        
        // Y من فوق: 0 = فوق، height = تحت
        // اللاعب عندو y = height - 100 (قريب من تحت)
        // الأعداء لازم يكونوا عند نفس مستوى اللاعب
        double groundLevel = height -310;  // تحت الشاشة
           int coinPercent = 30;      // 30% عملات
        int powerUpPercent = 15;   // 15% باور اب (عدلناها من 5% إلى 15%)
        int obstaclePercent = 15;  // 15% عوائق
        int enemyPercent = 40;
        double enemyY = groundLevel - 60;   // نفس مستوى اللاعب
        double coinY = groundLevel - 30;
          // زيادة الأعداء مع المستوى
        int finalEnemyPercent = Math.min(55, enemyPercent + level);
        int finalCoinPercent = coinPercent - (finalEnemyPercent - enemyPercent);
        int enemyChance = 50 + (level );
        
        if (rand < 35) {
            coins.add(new Coin(width, coinY));
        } 
        else if (rand < 25 + enemyChance) {
            if (random.nextInt(100) < 50) {
                enemies.add(new Enemy(width, enemyY));
            } 
            else if (level >= 2 && random.nextInt(100) < 80) {
                shootingEnemies.add(new ShootingEnemy(width, enemyY, enemyBullets));
            } 
            else {
                Enemy fast = new Enemy(width, enemyY);
                fast.enemyType = 1;
                enemies.add(fast);
            }
        } 
        else if (rand < 35 + enemyChance + 15) {
            obstacles.add(new Obstacle(width, groundLevel - 50, 20 + random.nextInt(25)));
        } 
        else if (rand < 35 + powerUpPercent) {
            powerUps.add(new PowerUp(width, coinY));
        }
        spawnCounter = 0;
    }
}
    
    private void checkLevelUp() {
        if (score >= scoreForNextLevel) {
            Sound.play("levelstart.wav");
            level++;
            scoreForNextLevel += 500;
            gameSpeed = Math.min(15, 5 + (level * 0.5));
            floatingTexts.add(new FloatingText(width/2 - 50, height/2, "LEVEL " + level + "!", 1, 1, 0));
            for (int i = 0; i < 30; i++) {
                particles.add(new Particle(width/2, height/2,
                    (random.nextDouble() - 0.5) * 15,
                    (random.nextDouble() - 0.5) * 15,
                    1, 0.8f, 0));
            }
        }
    }
    
    /**
     * FIXED: Proper collision detection between player and any object
     */
    private boolean checkCollision(Player player, double objX, double objY, double objW, double objH) {
        // Player bounds
        double playerLeft = player.x;
        double playerRight = player.x + player.width;
        double playerTop = player.y - player.height;
        double playerBottom = player.y;
        
        // Object bounds
        double objLeft = objX;
        double objRight = objX + objW;
        double objTop = objY;
        double objBottom = objY + objH;
        
        return playerRight > objLeft && 
               playerLeft < objRight && 
               playerBottom > objTop && 
               playerTop < objBottom;
    }
    
    private void createPickupParticles(double x, double y, float r, float g, float b) {
        for (int i = 0; i < 12; i++) {
            particles.add(new Particle(x, y, 
                (random.nextDouble() - 0.5) * 6,
                (random.nextDouble() - 0.5) * 6 - 3,
                r, g, b));
        }
    }
    
    private void createExplosionParticles(double x, double y) {
        for (int i = 0; i < 25; i++) {
            particles.add(new Particle(x, y,
                (random.nextDouble() - 0.5) * 10,
                (random.nextDouble() - 0.5) * 8 - 2,
                1, 0.2f, 0));
        }
    }
    
    private void handleInput(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            player.jump();
            Sound.play("jump.wav");
        } else if (e.getKeyCode() == KeyEvent.VK_X || e.getKeyCode() == KeyEvent.VK_CONTROL) {
            if (attackCooldown == 0) {
                if (weaponType == 0) {
                    bullets.add(new Bullet(player.x + player.width, player.y - player.height/2));
                    attackCooldown = 15;
                } else if (weaponType == 1) {
                    for (int i = -1; i <= 1; i++) {
                        Bullet b = new Bullet(player.x + player.width, player.y - player.height/2 + i * 8);
                        b.speed = 15;
                        bullets.add(b);
                    }
                    attackCooldown = 10;
                } else if (weaponType == 2) {
                    Iterator<Enemy> it = enemies.iterator();
                    while (it.hasNext()) {
                        Enemy enemy = it.next();
                        if (enemy.x > player.x) {
                            it.remove();
                            score += 20;
                            createExplosionParticles(enemy.x + enemy.width/2, enemy.y + enemy.height/2);
                        }
                    }
                    attackCooldown = 30;
                }
            }
        } else if (e.getKeyCode() == KeyEvent.VK_Q) {
            weaponType = (weaponType + 1) % 3;
            String weaponName = weaponType == 0 ? "Normal" : (weaponType == 1 ? "Fast" : "Laser");
            floatingTexts.add(new FloatingText(player.x + 30, player.y - 30, weaponName, 0, 1, 1));
        } else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            isRunningFast = true;
            normalGameSpeed = gameSpeed;
            gameSpeed = Math.min(15, gameSpeed + 4);
        }
    }
    
    private void handleKeyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            isRunningFast = false;
            gameSpeed = normalGameSpeed;
        }
    }
    
    private void renderInfoScreen(GL2 gl) {
        if (!showInfoScreen) return;
        
        gl.glEnable(GL2.GL_BLEND);
        gl.glColor4f(0, 0, 0, 0.85f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(0, 0);
        gl.glVertex2d(width, 0);
        gl.glVertex2d(width, height);
        gl.glVertex2d(0, height);
        gl.glEnd();
        
        TextRenderer.drawStringLarge(gl, "RUNNER GAME", width/2 - 100, height/2 - 150, 1, 0.8f, 0.2f);
        TextRenderer.drawString(gl, "THE CHASE", width/2 - 55, height/2 - 100);
        
        TextRenderer.drawString(gl, "Collect Coins ................... +10 Points", width/2 - 180, height/2 - 30);
        TextRenderer.drawString(gl, "Defeat Enemies ................. +30 to +120 Points", width/2 - 200, height/2 + 10);
        TextRenderer.drawString(gl, "Power-ups ....................... +50 Points or +1 Life", width/2 - 200, height/2 + 50);
        TextRenderer.drawString(gl, "Boss Fights ..................... Defeat Boss +200 Points", width/2 - 200, height/2 + 90);
        
        TextRenderer.drawString(gl, "Controls:", width/2 - 50, height/2 + 150);
        TextRenderer.drawString(gl, "SPACE = Jump     X = Shoot     Q = Weapon     SHIFT = Sprint", width/2 - 220, height/2 + 180);
        
        TextRenderer.drawString(gl, "Starting in " + (int)(infoScreenTimer / 60 + 1) + "...", width/2 - 60, height/2 + 240);
        
        gl.glDisable(GL2.GL_BLEND);
    }
    
    @Override
    public void render(GL2 gl, Game game) {
        if (showInfoScreen) {
            game.renderSky(gl);
            game.renderClouds(gl);
            game.renderGround(gl);
            renderInfoScreen(gl);
        } else {
            renderGame(gl, game);
            renderHUD(gl);
        }
    }
    
    private void renderGame(GL2 gl, Game game) {
        game.renderSky(gl);
        game.renderClouds(gl);
        game.renderGround(gl);
        
        player.render(gl);
        for (Coin c : coins) c.render(gl);
        for (Enemy e : enemies) e.render(gl);
        for (Obstacle o : obstacles) o.render(gl);
        for (PowerUp p : powerUps) p.render(gl);
        for (Bullet b : bullets) b.render(gl);
        for (Particle p : particles) p.render(gl);
        for (FloatingText ft : floatingTexts) ft.render(gl);
        for (Boss b : bosses) b.render(gl);
        for (ShootingEnemy s : shootingEnemies) s.render(gl);
        for (EnemyBullet eb : enemyBullets) eb.render(gl);
        for (FallingObstacle f : fallingObstacles) f.render(gl);
    }
    
    private void renderHUD(GL2 gl) {
        TextRenderer.drawString(gl, "Score: " + score, 10, 30);
        TextRenderer.drawString(gl, "Lives: " + lives, 10, 55);
        TextRenderer.drawString(gl, "Speed: " + String.format("%.1f", gameSpeed), 10, 80);
        TextRenderer.drawString(gl, "Level: " + level, 10, 105);
        
        for (int i = 0; i < lives; i++) {
            gl.glColor3f(1, 0, 0);
            gl.glBegin(GL2.GL_QUADS);
            gl.glVertex2d(10 + i * 25, 65);
            gl.glVertex2d(10 + i * 25 + 15, 65);
            gl.glVertex2d(10 + i * 25 + 15, 75);
            gl.glVertex2d(10 + i * 25, 75);
            gl.glEnd();
        }
        
        String readyText = (attackCooldown > 0) ? "Cooldown" : "READY - Press X";
        TextRenderer.drawStringRight(gl, readyText, width - 10, 50, 0, 1, 0);
        TextRenderer.drawStringRight(gl, "Enemies: " + enemies.size(), width - 10, 80, 1, 1, 1);
        
        String weaponName = weaponType == 0 ? "Normal" : (weaponType == 1 ? "Fast" : "Laser");
        TextRenderer.drawString(gl, "Weapon: " + weaponName, 10, height - 25);
        
        if (combo > 1) {
            TextRenderer.drawString(gl, "COMBO x" + comboMultiplier, width/2 - 70, 30);
        }
        
        String controls = "SPACE=Jump | P=Pause | X=Shoot | Q=Weapon";
        TextRenderer.drawString(gl, controls, 10, height - 10);
        
        if (player.invincible) {
            long remaining = Math.max(0, player.invincibleUntil - System.currentTimeMillis());
            int barWidth = (int)(100 * (remaining / 1500.0));
            gl.glColor3f(0, 1, 0);
            gl.glBegin(GL2.GL_QUADS);
            gl.glVertex2d(width - 120, height - 25);
            gl.glVertex2d(width - 120 + barWidth, height - 25);
            gl.glVertex2d(width - 120 + barWidth, height - 15);
            gl.glVertex2d(width - 120, height - 15);
            gl.glEnd();
            TextRenderer.drawString(gl, "Shield", width - 135, height - 18);
        }
    }
    
    @Override
    public void keyPressed(KeyEvent e, Game game) {
        if (e.getKeyCode() == KeyEvent.VK_P) {
            game.getStateManager().setState(new PauseState(this));
        } else {
            handleInput(e);
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e, Game game) {
        handleKeyReleased(e);
    }
}