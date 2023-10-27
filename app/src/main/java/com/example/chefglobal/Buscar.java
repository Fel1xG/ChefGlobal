package com.example.chefglobal;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Buscar extends Fragment {
    private RecyclerView recyclerView;
    private ComidasAdapter comidaAdapter;
    private SearchView searchView;

    private List<Comida> recetas; // Lista completa de recetas

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buscar, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        searchView = view.findViewById(R.id.searchView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializa la lista completa de recetas
        recetas = getRecetas();

        // Inicializa el adaptador con la lista completa
        comidaAdapter = new ComidasAdapter(recetas, getContext());
        recyclerView.setAdapter(comidaAdapter);

        // Agrega un TextWatcher al SearchView para detectar cambios en la búsqueda
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                realizarBusqueda(newText);
                return true;
            }
        });

        return view;
    }

    private void realizarBusqueda(String searchText) {
        // Filtra las recetas que contienen el texto de búsqueda
        List<Comida> resultados = new ArrayList<>();
        for (Comida receta : recetas) {
            if (receta.getNombre().toLowerCase().contains(searchText.toLowerCase())) {
                resultados.add(receta);
            }
        }

        // Crea un nuevo adaptador con los resultados de la búsqueda
        comidaAdapter = new ComidasAdapter(resultados, getContext());
        recyclerView.setAdapter(comidaAdapter);
    }

    // El método getRecetas() se mantiene igual
    private List<Comida> getRecetas() {
        List<Comida> recetas = new ArrayList<>();
        recetas.add(new Comida("Tacos al Pastor", "Carne de cerdo, tortillas de maíz, piña, cebolla, cilantro, salsa adobada"));
        recetas.add(new Comida("Pizza Margarita", "Masa de pizza, salsa de tomate, mozzarella, albahaca fresca, aceite de oliva"));
        recetas.add(new Comida("Ensalada César", "Lechuga romana, crutones, queso parmesano, pechuga de pollo, aderezo César"));
        recetas.add(new Comida("Sushi California", "Alga nori, arroz, aguacate, cangrejo de concha blanda, pepino, salsa de soja"));
        recetas.add(new Comida("Hamburguesa Clásica", "Carne de res, pan de hamburguesa, lechuga, tomate, cebolla, kétchup, mostaza"));
        recetas.add(new Comida("Tarta de Manzana", "Masa quebrada, manzanas, azúcar, canela, mantequilla"));
        recetas.add(new Comida("Pasta Alfredo", "Fettuccine, nata, mantequilla, queso parmesano, ajo, perejil"));
        recetas.add(new Comida("Pollo a la Parrilla", "Pechugas de pollo, aceite de oliva, pimentón, ajo, sal, pimienta"));
        recetas.add(new Comida("Tacos de Pescado", "Filetes de pescado, tortillas de maíz, repollo, crema agria, salsa de chipotle"));
        recetas.add(new Comida("Enchiladas Verdes", "Tortillas de maíz, pollo desmenuzado, salsa verde, queso, crema, cebolla"));
        recetas.add(new Comida("Pastel de Chocolate", "Harina, cacao, azúcar, huevo, leche, chocolate, vainilla"));
        recetas.add(new Comida("Sopa de Tomate", "Tomates, cebolla, ajo, caldo de pollo, albahaca, pan, aceite de oliva"));
        recetas.add(new Comida("Parrillada de Verduras", "Calabacín, pimiento, champiñones, aceite de oliva, ajo, hierbas"));
        recetas.add(new Comida("Ceviche de Camarones", "Camarones, limón, cebolla morada, cilantro, ají, aguacate"));
        recetas.add(new Comida("Tortilla Española", "Huevos, patatas, cebolla, aceite de oliva, sal"));
        recetas.add(new Comida("Sopa de Lentejas", "Lentejas, cebolla, zanahoria, apio, ajo, tomate, comino"));
        recetas.add(new Comida("Sándwich BLT", "Bacon, lechuga, tomate, mayonesa, pan de molde"));
        recetas.add(new Comida("Salmón a la Parrilla", "Filete de salmón, limón, ajo, eneldo, aceite de oliva"));
        recetas.add(new Comida("Fajitas de Pollo", "Pechugas de pollo, pimiento, cebolla, especias, tortillas"));
        recetas.add(new Comida("Curry de Pollo", "Pechugas de pollo, cebolla, tomate, leche de coco, curry"));
        recetas.add(new Comida("Risotto de Champiñones", "Arroz Arborio, champiñones, caldo de champiñones, vino blanco, queso Parmesano"));
        recetas.add(new Comida("Canelones de Carne", "Carne molida, pasta de canelones, salsa de tomate, bechamel, queso"));
        recetas.add(new Comida("Tarta de Limón", "Masa quebrada, limones, huevos, azúcar, nata, ralladura de limón"));
        recetas.add(new Comida("Sopa de Pollo y Maíz", "Pechugas de pollo, maíz, cebolla, pimiento, caldo de pollo"));
        recetas.add(new Comida("Papas Bravas", "Patatas, salsa brava, alioli"));
        recetas.add(new Comida("Gazpacho", "Tomates, pepino, pimiento, ajo, vinagre, aceite de oliva"));
        recetas.add(new Comida("Empanadas", "Masa de empanada, carne, cebolla, huevo, aceitunas, comino"));
        recetas.add(new Comida("Cazuela", "Carne de vacuno, zapallo, choclo, patas, zanahoria, arroz"));
        recetas.add(new Comida("Empanadas de Pino", "Carne molida, cebolla, pasas, aceitunas, huevo duro"));
        recetas.add(new Comida("Curanto", "Mariscos, papas, chapalele, milcaos, hojas de nalca"));
        recetas.add(new Comida("Pastel de Choclo", "Choclo, carne de pollo y vacuno, cebolla, huevo, aceitunas"));
        recetas.add(new Comida("Humitas", "Choclo, manteca, ají verde, cebolla, queso"));
        recetas.add(new Comida("Arrollado de Chancho", "Carne de cerdo, zanahoria, arvejas, huevo duro, perejil"));
        recetas.add(new Comida("Pebre", "Tomate, cebolla, ajo, cilantro, ají, limón"));
        recetas.add(new Comida("Porotos Granados", "Porotos verdes, zapallo, choclo, cebolla, albahaca, merquén"));
        recetas.add(new Comida("Sopaipillas", "Zapallo, harina, manteca, aceite"));
        recetas.add(new Comida("Chorrillana", "Carne, cebolla, papas fritas, huevo, aceite"));
        recetas.add(new Comida("Pan Amasado", "Harina, agua, levadura, sal, aceite"));
        recetas.add(new Comida("Milcao", "Papas, manteca, sal, cebolla"));
        recetas.add(new Comida("Sopa de Mariscos", "Mariscos variados, caldo, cebolla, ají, cilantro, vino blanco"));
        recetas.add(new Comida("Charquicán", "Carne, zapallo, papa, cebolla, arvejas, maíz"));
        recetas.add(new Comida("Asado", "Carne de res, cerdo, cordero, choripanes, cebolla a la parrilla"));
        recetas.add(new Comida("Paila Marina", "Pescados y mariscos variados, caldo de pescado, cebolla, tomate, cilantro"));
        recetas.add(new Comida("Chupe de Centolla", "Centolla, cebolla, pimiento, tomate, papas, queso, leche"));
        recetas.add(new Comida("Ternera a la Cazadora", "Ternera, cebolla, ajo, champiñones, vino tinto, laurel"));
        recetas.add(new Comida("Pantrucas", "Masa de pantrucas, caldo de pollo, cebolla, ajo, pimiento"));
        recetas.add(new Comida("Ajiaco Chilote", "Chochoca, papas, hortalizas, carne, longaniza, merquén"));
        recetas.add(new Comida("Paila en Olla", "Mariscos y pescados, papas, cebolla, ajo, pimiento, comino"));
        recetas.add(new Comida("Pollo Arvejado", "Pollo, arvejas, cebolla, ajo, pimiento, caldo de pollo"));
        recetas.add(new Comida("Lobos con Mayo", "Lobos, mayonesa, cebolla, cilantro, ajo, papas cocidas"));
        recetas.add(new Comida("Arroz con Congrio", "Congrio, arroz, cebolla, ajo, pimiento, cilantro"));
        recetas.add(new Comida("Sopa de Albóndigas", "Albóndigas de carne, caldo de pollo, cebolla, zanahoria, arroz"));
        recetas.add(new Comida("Jaibas al Coco", "Jaibas, coco rallado, cebolla, ajo, cilantro, ají"));
        recetas.add(new Comida("Guatitas a la Jardinera", "Guatitas de res, papas, zanahoria, arvejas, habas, choclo"));
        recetas.add(new Comida("Estofado de Cordero", "Cordero, papas, cebolla, zanahoria, choclo, arroz"));
        recetas.add(new Comida("Ensalada Chilena", "Tomates, cebolla, cilantro, aceite de oliva, vinagre, ají"));
        recetas.add(new Comida("Aguachile", "Camaroncillos, pepino, cebolla morada, chile, jugo de limón"));
        recetas.add(new Comida("Choros a la Chalaca", "Choros, cebolla, tomate, cilantro, ají, maíz"));
        recetas.add(new Comida("Sopa de Erizos", "Erizos de mar, cebolla, pimiento, tomate, papas"));
        recetas.add(new Comida("Piure al Pil Pil", "Piures, ajo, aceite de oliva, cilantro, ají"));
        recetas.add(new Comida("Lentejas con Longaniza", "Lentejas, longaniza, cebolla, ajo, zanahoria, comino"));
        recetas.add(new Comida("Costillar de Cordero al Horno", "Costillar de cordero, ajo, romero, vino tinto, miel"));
        recetas.add(new Comida("Chapaleles", "Masa de papa, cochayuyo, manteca"));
        recetas.add(new Comida("Caldillo de Congrio", "Congrio, papas, cebolla, pimiento, tomate"));
        recetas.add(new Comida("Leche Asada", "Leche, azúcar, vainilla, canela"));
        recetas.add(new Comida("Humita en Olla", "Choclo, manteca, ají verde, cebolla, queso"));
        recetas.add(new Comida("Asado a la Olla", "Carne, cebolla, zanahoria, papas, choclo"));
        recetas.add(new Comida("Mote con Huesillo", "Mote, huesillos, jugo de durazno, azúcar"));
        recetas.add(new Comida("Arrollado de Huaso", "Carne de cerdo, longaniza, ají, huevo, cebolla"));
        recetas.add(new Comida("Machas a la Parmesana", "Machas, queso parmesano, ajo, mantequilla"));
        recetas.add(new Comida("Bistec a lo Pobre", "Bistec, papas fritas, huevo frito, cebolla, ají"));
        recetas.add(new Comida("Charquicán de Pava", "Pava, zapallo, papas, cebolla, arvejas, maíz"));
        recetas.add(new Comida("Sopa de Pescado", "Pescado, papas, cebolla, tomate, cilantro, ají"));
        recetas.add(new Comida("Chorillana a lo Pobre", "Carne, cebolla, papas fritas, huevo, aceite"));
        recetas.add(new Comida("Machas a la Jardinera", "Machas, cebolla, pimiento, tomate, cilantro"));
        recetas.add(new Comida("Chairo", "Carne de vacuno, chuño, habas, zanahoria, arroz"));
        recetas.add(new Comida("Curanto en Hoyo", "Mariscos, carne, papas, hortalizas, chapalele, milcao"));
        recetas.add(new Comida("Calzones Rotos", "Masa, azúcar, huevo, esencia de vainilla"));
        recetas.add(new Comida("Chaparritas", "Masa de pantrucas, carne, cebolla, huevo duro"));
        recetas.add(new Comida("Sopa de Jibia", "Jibia, papas, zanahoria, zapallo, arroz, pimiento"));
        recetas.add(new Comida("Machas a la Crema", "Machas, crema, cebolla, ajo, vino blanco"));
        recetas.add(new Comida("Pantrucas con Pollo", "Pantrucas, pollo, cebolla, ajo, pimiento"));
        recetas.add(new Comida("Costillar de Cordero al Palo", "Costillar de cordero, palo, ajo, comino, romero"));
        recetas.add(new Comida("Tomaticán", "Carne de vacuno, tomates, cebolla, papas, comino"));
        recetas.add(new Comida("Arrollado de Chancho con Pebre", "Carne de cerdo, cebolla, ajo, cilantro, ají"));
        recetas.add(new Comida("Cola de Mono", "Aguardiente, café, azúcar, leche, vainilla"));
        recetas.add(new Comida("Sopa de Camarones", "Camarones, cebolla, ajo, pimiento, tomate"));
        recetas.add(new Comida("Sopa de Pavo", "Pavo, papas, cebolla, zanahoria, arvejas, arroz"));
        recetas.add(new Comida("Estofado de Pollo", "Pollo, cebolla, pimiento, papas, zanahoria"));
        recetas.add(new Comida("Cazuela de Ave", "Ave, papas, zapallo, choclo, arroz, caldo"));
        recetas.add(new Comida("Salsa de Cilantro", "Cilantro, ají, ajo, vinagre, aceite de oliva"));
        recetas.add(new Comida("Ceviche de Reineta", "Reineta, cebolla, cilantro, limón, ají"));
        recetas.add(new Comida("Pebre de Cochayuyo", "Cochayuyo, tomate, cebolla, cilantro, ají"));
        recetas.add(new Comida("Torta de Mil Hojas", "Masa de hojaldre, crema pastelera, merengue"));
        recetas.add(new Comida("Chacarero", "Churrasco, porotos verdes, ají verde, tomate"));
        recetas.add(new Comida("Calzones de la Dama", "Masa, zapallo, azúcar, nueces, pasas"));
        recetas.add(new Comida("Pescado Frito", "Pescado, limón, harina, aceite, ensalada"));
        recetas.add(new Comida("Salsa de Palta", "Palta, cebolla, ajo, cilantro, limón"));
        recetas.add(new Comida("Porotos con Riendas", "Porotos verdes, tallarines, choricillo, zapallo"));
        recetas.add(new Comida("Lomo a lo Pobre", "Lomo, papas fritas, huevo frito, cebolla"));
        recetas.add(new Comida("Carbonada", "Carne de vacuno, papas, choclo, zapallo, arroz"));
        recetas.add(new Comida("Carne Mechada", "Carne de res, ajo, cebolla, ají, cilantro"));
        recetas.add(new Comida("Curanto Chilote", "Mariscos, carne, papas, hortalizas, milcao"));
        recetas.add(new Comida("Tallarines Verdes", "Tallarines verdes, carne, albahaca, crema"));
        recetas.add(new Comida("Salmón al Coñac", "Salmón, coñac, cebolla, ajo, crema"));
        recetas.add(new Comida("Pimiento Relleno", "Pimientos, carne molida, cebolla, arroz, tomate"));
        recetas.add(new Comida("Zapallitos Italianos Rellenos", "Zapallitos, carne molida, cebolla, arroz, queso"));
        recetas.add(new Comida("Almejas a la Parmesana", "Almejas, queso parmesano, mantequilla, ajo, cilantro"));
        recetas.add(new Comida("Milanesa a la Napolitana", "Carne, jamón, queso, huevo, pan rallado"));
        recetas.add(new Comida("Paila de Leche", "Leche, arroz, azúcar, esencia de vainilla"));
        recetas.add(new Comida("Empanadas de Queso", "Masa de empanada, queso, cebolla, aceitunas"));
        recetas.add(new Comida("Caldo de Res", "Carne de res, verduras, fideos, caldo de carne"));
        recetas.add(new Comida("Caldo de Pollo", "Pollo, verduras, fideos, caldo de pollo"));
        recetas.add(new Comida("Caldo de Pescado", "Pescado, mariscos, verduras, caldo de pescado"));
        recetas.add(new Comida("Caldo de Verduras", "Variedad de verduras, caldo de vegetales"));
        recetas.add(new Comida("Caldo de Champiñones", "Champiñones, cebolla, ajo, caldo de champiñones"));
        recetas.add(new Comida("Caldo de Lentejas", "Lentejas, cebolla, zanahoria, apio, caldo de lentejas"));
        recetas.add(new Comida("Caldo de Garbanzos", "Garbanzos, cebolla, pimiento, tomate, caldo de garbanzos"));
        recetas.add(new Comida("Caldo de Tomate", "Tomates, cebolla, ajo, caldo de tomate"));
        recetas.add(new Comida("Caldo de Maíz", "Maíz, cebolla, ajo, caldo de maíz"));
        recetas.add(new Comida("Caldo de Albóndigas", "Albóndigas de carne, verduras, caldo de carne"));
        recetas.add(new Comida("Caldo de Espárragos", "Espárragos, cebolla, ajo, caldo de vegetales"));
        recetas.add(new Comida("Caldo de Pollo y Arroz", "Pollo, arroz, verduras, caldo de pollo"));
        recetas.add(new Comida("Caldo de Ternera", "Carne de ternera, verduras, caldo de carne"));
        recetas.add(new Comida("Caldo de Pollo y Maíz", "Pollo, maíz, cebolla, caldo de pollo"));
        recetas.add(new Comida("Caldo de Pollo y Lentejas", "Pollo, lentejas, cebolla, caldo de pollo"));
        recetas.add(new Comida("Caldo de Pollo y Champiñones", "Pollo, champiñones, cebolla, caldo de pollo"));
        recetas.add(new Comida("Caldo de Pollo y Tomate", "Pollo, tomates, cebolla, caldo de pollo"));
        recetas.add(new Comida("Caldo de Pollo y Espinacas", "Pollo, espinacas, cebolla, caldo de pollo"));
        recetas.add(new Comida("Caldo de Pavo", "Pavo, verduras, fideos, caldo de pavo"));
        recetas.add(new Comida("Caldo de Pavo y Arroz", "Pavo, arroz, verduras, caldo de pavo"));
        recetas.add(new Comida("Cazuela de Res", "Carne de res, zapallo, choclo, papas, zanahoria"));
        recetas.add(new Comida("Asado a la Parrilla", "Carne de res, cerdo, cordero, choripanes"));
        recetas.add(new Comida("Bistec a lo Pobre", "Bistec de res, papas fritas, huevo frito, cebolla caramelizada"));
        recetas.add(new Comida("Estofado de Cordero", "Carne de cordero, papas, zanahorias, arvejas"));
        recetas.add(new Comida("Lomo a la Pobre", "Lomo de res, papas fritas, huevo frito, cebolla caramelizada"));
        recetas.add(new Comida("Churrasco", "Bistec de res, tomate, palta, mayonesa, pan amasado"));
        recetas.add(new Comida("Prietas con Papas", "Prietas (morcillas), papas, cebolla, ají"));
        recetas.add(new Comida("Chorillana", "Carne de res, papas fritas, cebolla, huevo frito"));
        recetas.add(new Comida("Charquicán", "Carne de res, zapallo, papa, cebolla, arvejas, maíz"));
        recetas.add(new Comida("Costillar de Cerdo al Horno", "Costillar de cerdo, marinada, asado al horno"));
        recetas.add(new Comida("Cordero al Palo", "Carne de cordero, asado en estaca, condimentos"));
        recetas.add(new Comida("Chupe de Mariscos", "Mariscos variados, caldo, verduras, crema"));
        recetas.add(new Comida("Congrio a la Vinagreta", "Congrio, cebolla, cilantro, ajo, vinagre"));
        recetas.add(new Comida("Cecina", "Carne de cerdo, salazón, ahumado"));
        recetas.add(new Comida("Chupe de Pollo", "Pollo, arroz, leche, huevo, queso"));
        recetas.add(new Comida("Chupe de Camarones", "Camarones, arroz, leche, huevo, queso"));
        recetas.add(new Comida("Chupe de Zapallo", "Zapallo, maíz, leche, queso"));
        recetas.add(new Comida("Chupe de Locos", "Locos, arroz, leche, huevo, queso"));
        recetas.add(new Comida("Chupe de Choclo", "Choclo, leche, huevo, queso"));
        recetas.add(new Comida("Chupe de Abastero", "Carne de abastero, arroz, leche, queso"));
        return recetas;
    }
}
