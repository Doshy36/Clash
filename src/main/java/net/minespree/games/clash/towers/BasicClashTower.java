package net.minespree.games.clash.towers;

import net.citizensnpcs.api.npc.NPC;
import net.minespree.babel.Babel;
import net.minespree.cartographer.util.GameArea;
import net.minespree.games.clash.Clash;
import net.minespree.games.clash.ClashMapData;
import net.minespree.rise.teams.Team;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BasicClashTower extends ClashTower {

    public BasicClashTower(ClashMapData data, Team team, GameArea tower, GameArea area) {
        super(data, Babel.translate("cl_basictower"), team, tower, area, 2700.0F, 150.0F, 20);
    }

    @Override
    public void initialize() {
        super.initialize();

        data.getTargets().add(this);
    }

    @Override
    public void setNpcSkin(NPC npc) {
        Player player = (Player) npc.getEntity();
        player.setItemInHand(new ItemStack(Material.BOW));

        if(team.getWoolColour() == 11) {
            try {
                Clash.changeSkin(npc, "Princess", "eyJ0aW1lc3RhbXAiOjE1MTU2NjQ3MzQ0MzAsInByb2ZpbGVJZCI6IjdjZjc2MTFkYmY2YjQxOWRiNjlkMmQzY2Q4NzUxZjRjIiwicHJvZmlsZU5hbWUiOiJrYXJldGg5OTkiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzM2NjZmMjE1ODA0ZDhiNDA3MjViOWE2M2EwMmVkMjU5MTNhNTY4Y2I0ZDdhNGRjMzgzOTZjZWE4YzYyODZhIn19fQ==",
                        "BwN5m9F8nKB2DBZvGuuQVuDdnXsB2XJ1AySk0dBiBNLK1wWxdYrT7XW3HkDgrjThMs5UggbO2R+KLwp5zDsmlzyDK7cAt3PE988Eksw7R2JU1lcs5FHBgOr7XMMAYZ24WoW4q9oj9nL7w/dPwVJpDY2wx8zY3gVcrbn2pTCvvPTvxO9aj31MT1yAVl+hGGwZY01mvqvd0ltCMiIvhEpL1OEOKuOzFCsPz8YMoElj+7PGQDc9aQa/wbh20LlPu73TKCrwjzD8jO87ZR/xvInq9iBIV7fZoRELt8n7UkH5QF80pauYjg1guvtxBDoVjSaqBXY2esX1J+u5mkT8i4GmqbcaoMKhwyUyytgQ/7PFofW0VXtCvcdCBeEV4/xd6UNG9TFtzwPHQY4rTbz4Xu38Zb61k/l3chiFIrO9ER3wJkO7WeVwPGbLdwvnpWHIsy14AuWgTS7UmZVpvN8Q+btgPERU3mKBeNuDIX2eDUpj2hLb8oxfsdqRjMC+rggQoE+a6AXxVrR/ogFGNxOiFxfoX4qgHic7Fxw2QGglxN70FGD4wA9ehKCCw+5cKMMf1R3AqRy4h8M/y00oXPecIYqc0mLoHIFuc8e9PT8dLlRtJ4e6psAxx+d0V/Raw0oV/rxE8dtCpCm7bELf3SH8hUIVlteDuTBxZpT7DZJcX+UYWBA=");
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Clash.changeSkin(npc, "Princess", "eyJ0aW1lc3RhbXAiOjE1MTU2NjQyNTE4ODEsInByb2ZpbGVJZCI6ImFkMWM2Yjk1YTA5ODRmNTE4MWJhOTgyMzY0OTllM2JkIiwicHJvZmlsZU5hbWUiOiJGdXJrYW5iejAwIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS80MzdjMDM5NzcwMzU4YTlkZTYzZDI2MWRjZDIyMTVmNjE5NDM2M2ZkMzViZDgxNzRkNmRhOWE1M2Q5YTEyOSJ9fX0=",
                        "ECx9ZRqr0AtQfgHpMJlc7WqEG5JOIoTslmxuE1my2YHOuGaqYIawQbgq/JviEWr8DorJSFXnYR3Br2290iGiyDIHS9wUrP/Ayw3kUAuXMLA7EvhTzCu04A5VL5sv6+mhWpbwK4R/2FmeRAycY0f+BV/BzgdeEHK9qC9pKTy6IMblKGmMdtUpwvdc4yPXGt3r44R9KbDc5d0tgvJrNCpnDq8XAbjPn4nUlLKuhrqvOCkMI4VHo8+GDps91vF974flfFMeiHZRdwcQzn/hMGnHd6QktbeOc/4RdhTnqH0q+IOxjRxb3rhqBWkjIxRFaZkOKJOvsvbFY4yF9cfvoFubzQJxtOFMrO1lyJJDtLYWoMy2/e5IyEgS5h+qK7Bn5g4qPrMaOBjZurL04lJvgWpUcgwuZ+2An8H2dEBJK+IBddMHm3y6S6U0ZEkFQHfOmVw5Cq6KLdfjagHCTdkyTlOfsC92TTq8TzeCwM/KjHCfjCBf5+va38HE8aMDVfA8ehCHSKcx/IwI0/7MKkMPQrRkEYYVj1l75ZEGPAk/qAN3SGhQ6dLMeVF4w7ZRBkuQW9YsjUIPCI7cTmN422iYp8hahcni6vMVJuWCNg4AdLgCOH5nLr4JRpwVBYkZC3DqLvsypoR3NDXGpy+eoK1W6NVe9EqM+XZxXjgK8PVQOWbNS+8=");
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void kill(Player killer) {
        super.kill(killer);

        for (int i = 0; i < 3; i++) {
            location.getWorld().createExplosion(location.add(0, 1, 0), 5.0F);
        }

        data.getTowers().stream().filter(tower -> tower instanceof CastleClashTower && tower.getTeam() == team).forEach(tower -> data.getTargets().add(tower));
    }
}
